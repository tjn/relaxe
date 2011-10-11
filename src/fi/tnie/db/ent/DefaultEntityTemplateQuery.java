/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
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
import fi.tnie.db.expr.OrderBy;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.SelectStatement;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.OrderBy.Order;
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
	implements EntityQuery<A, R, T, E, H, F, M, Q>	
{
	
//	private static Logger logger = Logger.getLogger(DefaultEntityQuery.class);
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5505364328412305185L;
	private T type;
	
	private transient DefaultTableExpression query;
	private transient QueryExpression queryExpression;
	private transient TableReference rootRef;
		
	private transient List<Predicate> predicateList;

	private transient Map<TableReference, EntityMetaData<?, ?, ?, ?, ?, ?, ?>> metaDataMap;
	
	private transient LinkedHashMap<Integer, TableReference> originMap;	
	private transient Map<JoinKey, TableReference> referenceMap;
	
	
	
	private transient Map<EntityQueryTemplateAttribute, ColumnReference> columnMap;
	
	private transient Map<EntityQuerySortKey<?>, ColumnReference> sortKeyColumnMap = new HashMap<EntityQuerySortKey<?>, ColumnReference>();	
	private transient Map<EntityQueryPredicate<?>, ColumnReference> predicateColumnMap = new HashMap<EntityQueryPredicate<?>, ColumnReference>();
	
	private transient List<ColumnReference> rootPrimaryKey; // NS	
	private Q template;
					
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DefaultEntityTemplateQuery() {
	}
	
	public DefaultEntityTemplateQuery(Q rootTemplate) {
		
		if (rootTemplate == null) {
			throw new NullPointerException();
		}
		
		this.template = rootTemplate;
		this.type = rootTemplate.getMetaData().getType();
	}

	public DefaultEntityTemplateQuery(Q root, boolean init) 
		throws CyclicTemplateException, EntityRuntimeException {
		this(root);
	
		if (init) {
			init();
		}
	}

	private void init() throws CyclicTemplateException, EntityRuntimeException {
		if (isInitialized()) {
			return;
		}
		
		Q root = this.template;
				
			
		BaseTable table = getMetaData().getBaseTable();

		if (table == null) {
			throw new NullPointerException("EntityMetaData.getBaseTable()");
		}		
	
		DefaultTableExpression q = new DefaultTableExpression();
		HashSet<EntityQueryTemplate<?,?,?,?,?,?,?,?>> visited = new HashSet<EntityQueryTemplate<?,?,?,?,?,?,?,?>>();

		AbstractTableReference tref = fromTemplate(root, null, null, null, q, visited);
		q.setFrom(new From(tref));
				
		this.query = q;
		
		List<EntityQuerySortKey<?>> sortKeyList = root.allSortKeys();
					
		if (sortKeyList.isEmpty()) {
			this.queryExpression = this.query;
		}		
		else {
			OrderBy ob = q.getOrderBy();
			
			if (ob == null) {
				ob = new OrderBy();
				
				for (EntityQuerySortKey<?> sk : sortKeyList) {
					ColumnReference cr = sortKeyColumnMap.get(sk);										
					// Only template attributes which are used as sort keys have associated column reference.
					// Other sort keys just do without.					    					
					ob.add(sk.sortKey(cr));
				}
			}
			
			for (ColumnReference pkcol : getRootPrimaryKey()) {
				ob.add(pkcol, Order.ASC);
			}

//			Limit le = (this.limit == null) ? null : new Limit(limit.longValue());
//			Offset oe = (this.offset == null) ? null : new Offset(this.offset.longValue());
			
			SelectStatement sq = new SelectStatement(q, ob, null, null);			
			this.queryExpression = sq;
		}
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
		
		final boolean root = (qref == null);
		
		final TableReference tref = (qref == null) ? getTableRef() : new TableReference(meta.getBaseTable());		
		getMetaDataMap().put(tref, meta);
				
		if (referencing != null) {
			JoinKey j = new JoinKey(referencing, fk);
			getReferenceMap().put(j, tref);
		}
		
		if (qref == null) {
			qref = tref;
		}
		else {
			ForeignKeyJoinCondition jc = new ForeignKeyJoinCondition(fk, referencing, tref);
			qref = qref.leftJoin(tref, jc);
		}
					
		Set<Column> pkcols = meta.getPKDefinition();
		
		for (Column c : pkcols) {
			ColumnReference cref = new ColumnReference(tref, c);
			s.add(cref);
			
			if (root) {
				getRootPrimaryKey().add(cref);
			}
		}					

		addAttributes(template, s, tref);
		
		
		List<EntityQueryPredicate<MA>> ps = template.predicates();
		
		for (EntityQueryPredicate<MA> k : ps) {
			MA a = k.attribute();
			ColumnReference cref = null;
			
			if (a != null) {			
				Column c = meta.getColumn(a);
				cref = new ColumnReference(tref, c);
				predicateColumnMap.put(k, cref);
			}
		}		
		
		List<EntityQuerySortKey<MA>> keys = template.sortKeys();
		
		for (EntityQuerySortKey<MA> k : keys) {
			MA a = k.attribute();
			ColumnReference cref = null;
			
			if (a != null) {			
				Column c = meta.getColumn(a);
				cref = new ColumnReference(tref, c);
				sortKeyColumnMap.put(k, cref);
			}
		}
		
		getOriginMap().put(Integer.valueOf(s.getColumnCount()), tref);
		
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
			
			getColumnMap().put(ta, cref);
			
			if (ta.isSelected(cref)) {
				s.add(cref);
			}
			
			Predicate p = ta.createPredicate(cref);			
			addPredicate(p);
									
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
	
	@Override
	public DefaultTableExpression getTableExpression() 
		throws CyclicTemplateException, EntityRuntimeException {
		init();		
		return this.query;
	}

	@Override
	public QueryExpression getQueryExpression() 
		throws CyclicTemplateException, EntityRuntimeException {
		init();
		
		return this.queryExpression;
	}
	
	/**
	 * Returns the root table-reference for this query.
	 * @return
	 */
	@Override
    public TableReference getTableRef() {
        if (this.rootRef == null) {
            this.rootRef = new TableReference(getMetaData().getBaseTable());
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
		return this.type.getMetaData();		
	}

	@Override
	public EntityMetaData<?, ?, ?, ?, ?, ?, ?> getMetaData(TableReference tref) 
		throws CyclicTemplateException, EntityRuntimeException {
		if (tref == null) {
			throw new NullPointerException("tref");
		}
		
		init();
		
		return getMetaDataMap().get(tref);
	}

	/**
	 * Returns the table reference which the specified <code>column</code> originates from.
	 * Column numbering starts from 1.
	 * 
	 * Throws {@link IndexOutOfBoundsException} if column &lt; 1. 
	 */	
	@Override
	public TableReference getOrigin(int column) 
		throws CyclicTemplateException, EntityRuntimeException {	
		if (column < 1) {
			throw new IndexOutOfBoundsException();
		}
		
		init();
		
		Map<Integer, TableReference> om = getOriginMap();
				
		for (Integer k : om.keySet()) {
			if (column <= k.intValue()) {
				return om.get(k);
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
		
	@Override
	public TableReference getReferenced(TableReference referencing, ForeignKey fk) {
		JoinKey k = new JoinKey(referencing, fk);
		TableReference r = getReferenceMap().get(k);
		return r;
	}

	private static class JoinKey
		implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2839759478114689320L;
		private TableReference referencing;	
		private ForeignKey foreignKey;
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private JoinKey() {	
		}
		
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
	
	
//	private List<EntityQuerySortKey> getSortKeyList() {
//		if (sortKeyList == null) {
//			sortKeyList = new ArrayList<EntityQuerySortKey>();			
//		}
//
//		return sortKeyList;
//	}
	
//	private boolean addSortKey(EntityQuerySortKey sk) {
//		if (sk == null) {
//			return false;
//		}
//		
//		return getSortKeyList().add(sk);
//	}
	
	private List<ColumnReference> getRootPrimaryKey() {
		if (rootPrimaryKey == null) {
			rootPrimaryKey = new ArrayList<ColumnReference>();			
		}

		return rootPrimaryKey;
	}
	
	public Q getTemplate() {
		return template;
	}
	
	private Map<Integer, TableReference> getOriginMap() {
		if (originMap == null) {
			originMap = new LinkedHashMap<Integer, TableReference>();			
		}

		return originMap;
	}
	
	
	private Map<JoinKey, TableReference> getReferenceMap() {
		if (referenceMap == null) {
			referenceMap = new HashMap<JoinKey, TableReference>();
		}

		return referenceMap;		
	}
	
	private boolean isInitialized() {
		return (this.queryExpression != null);
	}
		
	public Map<EntityQueryTemplateAttribute, ColumnReference> getColumnMap() {
		if (columnMap == null) {
			columnMap = new HashMap<EntityQueryTemplateAttribute, ColumnReference>();			
		}

		return columnMap;
	}	
}

