/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

//import org.apache.log4j.Logger;

import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.expr.AbstractTableReference;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.ForeignKeyJoinCondition;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public class DefaultEntityTemplateQuery<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	Q extends EntityQueryTemplate<A, R, T, E, H, F, M, Q>
	> 
	implements EntityQuery<A, R, T, E, M>
{
	
//	private static Logger logger = Logger.getLogger(DefaultEntityQuery.class);
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5505364328412305185L;
	private M meta;
	private DefaultTableExpression query;
	private QueryExpression queryExpression;
	private TableReference rootRef;
	
	private List<Predicate> predicateList;
	private List<EntityQuerySortKey> sortKeyList;
	
	private Map<TableReference, EntityMetaData<?, ?, ?, ?, ?, ?, ?>> metaDataMap;
	
	private LinkedHashMap<Integer, TableReference> originMap = new LinkedHashMap<Integer, TableReference>();	
	private Map<JoinKey, TableReference> referenceMap = new HashMap<JoinKey, TableReference>();
				
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DefaultEntityTemplateQuery() {
	}

	public DefaultEntityTemplateQuery(Q root)
		throws CyclicTemplateException, EntityRuntimeException {
		super();
		init(root);
	}

	private void init(Q root) throws CyclicTemplateException, EntityRuntimeException {
		if (root == null) {
			throw new NullPointerException();
		}
				
		M meta = root.getMetaData();
		BaseTable table = meta.getBaseTable();

		if (table == null) {
			throw new NullPointerException("EntityMetaData.getBaseTable()");
		}

		this.meta = meta;

		DefaultTableExpression q = new DefaultTableExpression();
		HashSet<EntityQueryTemplate<?,?,?,?,?,?,?,?>> visited = new HashSet<EntityQueryTemplate<?,?,?,?,?,?,?,?>>();

		AbstractTableReference tref = fromTemplate(root, null, null, null, q, visited);
		q.setFrom(new From(tref));
				
		this.query = q;
		
		// TODO: decorate with ordering:
		this.queryExpression = this.query;
	}


	private 
	<
		MA extends Attribute,
		MR extends Reference,
		MT extends ReferenceType<MA, MR, MT, ME, MH, MF, MM>,
		ME extends Entity<MA, MR, MT, ME, MH, MF, MM>,
		MH extends ReferenceHolder<MA, MR, MT, ME, MH, MM>,		
		MF extends EntityFactory<ME, MH, MM, MF>,		
		MM extends EntityMetaData<MA, MR, MT, ME, MH, MF, MM>,
		MQ extends EntityQueryTemplate<MA, MR, MT, ME, MH, MF, MM, MQ>
	>
	AbstractTableReference fromTemplate(
			MQ template, 
			AbstractTableReference qref, ForeignKey fk, TableReference referencing, 
			DefaultTableExpression q,
			Set<EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?>> visited)
		throws CyclicTemplateException, EntityRuntimeException {
		
		if (visited.contains(template)) {
			throw new CyclicTemplateException(template);
		}
		else {
			visited.add(template);
		}		
		
		Select s = getSelect(q);
		MM meta = template.getMetaData();		
		
		final TableReference tref = (qref == null) ? getTableRef() : new TableReference(meta.getBaseTable());		
		getMetaDataMap().put(tref, meta);
				
		if (referencing != null) {
			JoinKey j = new JoinKey(referencing, fk);
			referenceMap.put(j, tref);
		}		
		
		if (qref == null) {
			qref = tref;
		}
		else {
			ForeignKeyJoinCondition jc = new ForeignKeyJoinCondition(fk, qref, tref);
			qref = qref.leftJoin(tref, jc);
		}
					
		Set<Column> pkcols = meta.getPKDefinition();
		
		for (Column c : pkcols) {
			ColumnReference cref = new ColumnReference(tref, c);
			s.add(cref);
		}
						
//		Set<MR> rs = meta.relationships();

//		for (MR r : rs) {
//			EntityKey<?, ?, ?, M, ?, ?, ?, ?> k = meta.getEntityKey(r);			
//			ReferenceHolder<?, ?, ?, ?> h = k.get(template);
//			
//			if (h == null) {
//				// skip
//			}
//			else {
//				fk = meta.getForeignKey(r);
//				
//				for (Column c : fk.columns().keySet()) {
//					s.add(new ColumnReference(tref, c));
//				}
//			}
//		}

		addAttributes(template, s, tref);				
		originMap.put(Integer.valueOf(s.getColumnCount()), tref);
		
		qref = processReferences(template, qref, tref, q, visited);
						
		return qref;
	}
	
	private	<
		KA extends Attribute,
		KR extends Reference,
		KT extends ReferenceType<KA, KR, KT, KE, ?, ?, KM>,
		KE extends Entity<KA, KR, KT, KE, ?, ?, KM>,	
		KM extends EntityMetaData<KA, KR, KT, KE, ?, ?, KM>,
		KQ extends EntityQueryTemplate<KA, KR, KT, KE, ?, ?, KM, KQ>		
	>
	AbstractTableReference processReferences(KQ template, AbstractTableReference qref, TableReference tref, DefaultTableExpression q, Set<EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?>> visited) throws EntityRuntimeException, CyclicTemplateException {

		KM meta = template.getMetaData();
		
		Set<KR> rs = meta.relationships();
		
		for (KR kr : rs) {
			EntityKey<KR, KT, KE, KM, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(kr);
			EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?> t = template.getTemplate(ek);
			
			if (t != null) {				
				ForeignKey fk = meta.getForeignKey(kr);

				if (fk == null) {
					throw new NullPointerException("can not find fk by relationship: " + kr);
				}							
				
				qref = fromTemplate(t.self(), qref, fk, tref, q, visited);
			}									
		}
	
		return qref;
	
	}

	private Select getSelect(DefaultTableExpression q) {
		Select s = q.getSelect();

		if (s == null) {
			q.setSelect(s = new Select());
		}
		
		return s;
	}

	private <
		MA extends Attribute,
		D extends Entity<MA, ?, ?, D, ?, ?, DM>,
		DM extends EntityMetaData<MA, ?, ?, D, ?, ?, DM>,
		DQ extends EntityQueryTemplate<MA, ?, ?, D, ?, ?, DM, DQ>
	> 
	void addAttributes(DQ template, Select s, TableReference tref) throws EntityRuntimeException {
		DM meta = template.getMetaData();
		Set<MA> as = meta.attributes();
						
		for (MA a : as) {
			EntityQueryTemplateAttribute ta = template.get(a);
			
			if (ta == null) {
				continue;
			}
			
			Column c = meta.getColumn(a);
			ColumnReference cref = new ColumnReference(tref, c);
			
			if (ta.isSelected(cref)) {
				s.add(cref);
			}
			
			Predicate p = ta.createPredicate(cref);			
			addPredicate(p);
						
			EntityQuerySortKey sk = ta.createSortKey(cref);
			addSortKey(sk);			
									
//			PrimitiveHolder<?, ?> h = template.value(a);
//						
//			if (h != null) {
//				Column c = meta.getColumn(a);
//				
//				// primary column are added separately:				
//				if (c != null && c.isPrimaryKeyColumn() == false) {
//					s.add(new ColumnReference(tref, c));
//				}
//			}
		}
	}

	private boolean addPredicate(Predicate p) {
		if (p == null) {
			return false;
		}
		
		return getPredicateList().add(p);
	}
	
	public DefaultTableExpression getTableExpression() {
		return this.query;
	}

	public QueryExpression getQueryExpression() {
		return this.queryExpression;
	}
	
	/**
	 * Returns the root table-reference for this query.
	 * @return
	 */
    public TableReference getTableRef() {
        if (this.rootRef == null) {
            this.rootRef = new TableReference(meta.getBaseTable());
        }

        return this.rootRef;
    }

//	@Override
//	public Long getLimit() {
//		return null;
//	}
//
//	@Override
//	public int getOffset() {
//		return 0;
//	}

	@Override
	public M getMetaData() {
		return this.meta;
	}

	@Override
	public EntityMetaData<?, ?, ?, ?, ?, ?, ?> getMetaData(TableReference tref) {
		if (tref == null) {
			throw new NullPointerException("tref");
		}
		
		return getMetaDataMap().get(tref);
	}

	@Override
	public TableReference getOrigin(int column) {	
		if (column < 1) {
			throw new IndexOutOfBoundsException();
		}

		for (Integer k : originMap.keySet()) {
			if (column <= k.intValue()) {
				return originMap.get(k);
			}
		}	
		
		return null;
	}

	private Map<TableReference, EntityMetaData<?, ?, ?, ?, ?, ?, ?>> getMetaDataMap() {
		if (metaDataMap == null) {
			metaDataMap = new HashMap<TableReference, EntityMetaData<?,?,?,?, ?, ?, ?>>();			
		}

		return metaDataMap;
	}
		
	public TableReference getReferenced(TableReference referencing, ForeignKey fk) {
		JoinKey k = new JoinKey(referencing, fk);
		TableReference r = this.referenceMap.get(k);
		return r;
	}

	private static class JoinKey {
		private TableReference referencing;	
		private ForeignKey foreignKey;
		
		public JoinKey(TableReference referencing, ForeignKey foreignKey) {
			super();
			this.referencing = referencing;			
			this.foreignKey = foreignKey;
		}
		
		@Override
		public int hashCode() {		
			return referencing.hashCode() ^ foreignKey.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				throw new NullPointerException("obj");
			}
						
			JoinKey j = (JoinKey) obj;
			
			return 
				this.referencing.equals(j.referencing) &&
				this.foreignKey.equals(j.foreignKey);
		}
	}

//	private static Logger logger() {
//		return DefaultEntityQuery.logger;
//	}
	

	private List<Predicate> getPredicateList() {
		if (predicateList == null) {
			predicateList = new ArrayList<Predicate>();
			
		}

		return predicateList;
	}
	
	
	private List<EntityQuerySortKey> getSortKeyList() {
		if (sortKeyList == null) {
			sortKeyList = new ArrayList<EntityQuerySortKey>();			
		}

		return sortKeyList;
	}
	
	private boolean addSortKey(EntityQuerySortKey sk) {
		if (sk == null) {
			return false;
		}
		
		return getSortKeyList().add(sk);		
	}
}

