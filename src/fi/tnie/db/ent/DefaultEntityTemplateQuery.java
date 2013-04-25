/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.expr.AbstractTableReference;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.MultiForeignKeyJoinCondition;
import fi.tnie.db.expr.OrderBy;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.SelectStatement;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.Where;
import fi.tnie.db.expr.OrderBy.Order;
import fi.tnie.db.expr.op.AndPredicate;
import fi.tnie.db.log.DefaultLogger;
import fi.tnie.db.log.Logger;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public class DefaultEntityTemplateQuery<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	Q extends EntityQueryTemplate<A, R, T, E, H, F, M, C, Q>
	> 
	implements EntityQuery<A, R, T, E, H, F, M, C, Q>	
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

	private transient Map<TableReference, EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?>> metaDataMap;
	
	private transient LinkedHashMap<Integer, TableReference> originMap;	
	private transient Map<JoinKey, TableReference> referenceMap;
	
	private transient Map<EntityQueryTemplateAttribute, ColumnReference> columnMap;
	
	private transient Map<EntityQuerySortKey<?>, ColumnReference> sortKeyColumnMap = new HashMap<EntityQuerySortKey<?>, ColumnReference>();	
	private transient Map<EntityQueryPredicate<?>, ColumnReference> predicateColumnMap = new HashMap<EntityQueryPredicate<?>, ColumnReference>();
	
	private transient Map<EntityQueryPredicate<?>, TableReference> tableReferenceMap = new HashMap<EntityQueryPredicate<?>, TableReference>();
	
	private transient List<ColumnReference> rootPrimaryKey; // NS	
		
	public static final class Entry {				
		private Map<ForeignKey, TableReference> referenceMap;
		
		public Entry() {
			super();			
		}
		
		public Map<ForeignKey, TableReference> getReferenceMap() {
			if (referenceMap == null) {
				referenceMap = new HashMap<ForeignKey, TableReference>();				
			}

			return referenceMap;
		}
	}
	
	
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
		this.type = rootTemplate.getMetaData().type();
	}

//	private DefaultEntityTemplateQuery(Q root, boolean init) 
//		throws CyclicTemplateException, EntityRuntimeException {
//		this(root);
//	
//		if (init) {
//			init();
//		}
//	}

	private void init() throws CyclicTemplateException, EntityRuntimeException {
		if (isInitialized()) {
			return;
		}
		
		logger().debug("init - enter");
		
		Q root = this.template;				
			
		BaseTable table = getMetaData().getBaseTable();

		if (table == null) {
			throw new NullPointerException("DefaultEntityTemplateQuery: getMetaData().getBaseTable()");
		}		
				
		List<EntityQueryPredicate<?>> apl = new ArrayList<EntityQueryPredicate<?>>();
		
		{
			Set<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>> visited = 
				new HashSet<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>>();		
			addTemplatePredicates(root, apl, visited);
		}
			
		DefaultTableExpression te = new DefaultTableExpression();
		
		Set<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>> visited = 
			new HashSet<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>>();
		
		Map<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>, TableReference> trm = 
			new HashMap<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>, TableReference>();
		
		Map<EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?>, Entry> rem = 
			new HashMap<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>, Entry>();
			
		this.rootRef = populateTableReferenceMap(root, trm, rem);		

		AbstractTableReference tref = fromTemplate(null, root, null, null, null, te, visited, trm, rem);
		
		logger().debug("ref: " + tref);		
		logger().debug("originMap: " + originMap);
		logger().debug("metaDataMap: " + metaDataMap);
		
		te.setFrom(new From(tref));
								
		List<EntityQueryPredicate<?>> pl = apl;		
		
		if (pl != null && (!pl.isEmpty())) {
			
//			EntityQueryPredicateContext pc = new EntityQueryPredicateContext() {				
//				@Override
//				public TableReference getTableReference(EntityQueryPredicate<?> p) {
//					return tableReferenceMap.get(p);
//				}
//			};
						
			Predicate ap = null;
			
			for (EntityQueryPredicate<?> p : pl) {
				TableReference ptr = tableReferenceMap.get(p);				
				ColumnReference cr = predicateColumnMap.get(p);				
				Predicate qp = p.predicate(ptr, cr);
				ap = AndPredicate.newAnd(ap, qp);
			}
			
			te.setWhere(new Where(ap));
		}	
		
		
				
		this.query = te;
		
		List<EntityQuerySortKey<?>> sortKeyList = root.allSortKeys();
					
		if (sortKeyList.isEmpty()) {
			this.queryExpression = this.query;
		}		
		else {
			OrderBy ob = te.getOrderBy();
			
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
			
			SelectStatement sq = new SelectStatement(te, ob, null, null);			
			this.queryExpression = sq;
		}
		
		logger().debug("init - exit");
	}
	
	
	
	private <
		QA extends Attribute,
		QR extends Reference,
		QT extends ReferenceType<QA, QR, QT, QE, QH, QF, QM, QC>,
		QE extends Entity<QA, QR, QT, QE, QH, QF, QM, QC>,
		QH extends ReferenceHolder<QA, QR, QT, QE, QH, QM, QC>,
		QF extends EntityFactory<QE, QH, QM, QF, QC>,
		QM extends EntityMetaData<QA, QR, QT, QE, QH, QF, QM, QC>,
		QC extends Content,
		QQ extends EntityQueryTemplate<QA, QR, QT, QE, QH, QF, QM, QC, QQ>
	>
	
	void addTemplatePredicates(QQ qt, List<EntityQueryPredicate<?>> apl, Set<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>> visited) {
		apl.addAll(qt.allPredicates());
		
		QM meta = qt.getMetaData();		
		Set<QR> rs = meta.relationships();
		
		for (QR qr : rs) {
			EntityKey<QA, QR, QT, QE, QH, QF, QM, QC, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = meta.getEntityKey(qr);
			EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> t = qt.getTemplate(k);
			
			if (t != null) {
				if (!visited.contains(t)) {
					visited.add(t);
					addTemplatePredicates(t.self(), apl, visited);
				}
			}
		}
	}
	
	private <
		QA extends Attribute,
		QR extends Reference,
		QT extends ReferenceType<QA, QR, QT, QE, QH, QF, QM, QC>,
		QE extends Entity<QA, QR, QT, QE, QH, QF, QM, QC>,
		QH extends ReferenceHolder<QA, QR, QT, QE, QH, QM, QC>,
		QF extends EntityFactory<QE, QH, QM, QF, QC>,
		QM extends EntityMetaData<QA, QR, QT, QE, QH, QF, QM, QC>,
		QC extends Content,
		QQ extends EntityQueryTemplate<QA, QR, QT, QE, QH, QF, QM, QC, QQ>
	>	
	TableReference populateTableReferenceMap(
			QQ template, 
			Map<EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?>, TableReference> rm,
			Map<EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?>, Entry> rem) {
		TableReference tref = rm.get(template);
		
		if (tref != null) {
			// already visited
			return tref;			
		}
		
		QM meta = template.getMetaData();		
		tref = new TableReference(meta.getBaseTable());		
		rm.put(template, tref);
		
		getMetaDataMap().put(tref, meta);
		
		Set<QR> rs = meta.relationships();
		
		for (QR qr : rs) {
			EntityKey<QA, QR, QT, QE, QH, QF, QM, QC, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = meta.getEntityKey(qr);
			EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> t = template.getTemplate(k);
			
			if (t != null) {								
				TableReference rr = populateTableReferenceMap(t.self(), rm, rem);
				Entry e = rem.get(template);
				
				if (e == null) {
					e = new Entry();
					rem.put(template, e);
				}
				
				ForeignKey fk = meta.getForeignKey(qr);				
				e.getReferenceMap().put(fk, rr);
				
				JoinKey j = new JoinKey(tref, fk);
				getReferenceMap().put(j, rr);				
			}					
		}
		
		return tref;
	}


	private 
	<
		MA extends Attribute,
		MR extends Reference,
		MT extends ReferenceType<MA, MR, MT, ME, MH, MF, MM, MC>,
		ME extends Entity<MA, MR, MT, ME, MH, MF, MM, MC>,
		MH extends ReferenceHolder<MA, MR, MT, ME, MH, MM, MC>,		
		MF extends EntityFactory<ME, MH, MM, MF, MC>,		
		MM extends EntityMetaData<MA, MR, MT, ME, MH, MF, MM, MC>,
		MC extends Content,
		MQ extends EntityQueryTemplate<MA, MR, MT, ME, MH, MF, MM, MC, MQ>
	>
	AbstractTableReference fromTemplate(
			EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> parent,
			MQ template, 
			AbstractTableReference qref, 
			ForeignKey fk, 
			TableReference referencing, 
			DefaultTableExpression q,
			Set<EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?>> visited,
			Map<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>, TableReference> rm,
			Map<EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?>, Entry> rem)
		throws CyclicTemplateException, EntityRuntimeException {
		
		logger().debug("fromTemplate - enter: " + template);
		logger().debug("fromTemplate - fk: " + fk);
		
		if (visited.contains(template)) {
			return qref;
		}

		visited.add(template);		
		
//		visited.put(template, qref);
		
		Select s = getSelect(q);
		MM meta = template.getMetaData();
		
		final boolean root = (qref == null);
		
		final TableReference tref = rm.get(template);
				
		if (qref == null) {
			qref = tref;
		}
		else {
			Entry e = rem.get(parent);
			
			if (e != null) {			
				Map<ForeignKey, TableReference> krm = e.getReferenceMap();				
				qref = join(qref, tref, template, referencing, krm);
			}
		}
					
		Collection<Column> pkcols = meta.getBaseTable().getPrimaryKey().getColumnMap().values();
		
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
						
			tableReferenceMap.put(k, tref);
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
		
		qref = processReferences(template, qref, tref, q, visited, rm, rem);
						
		return qref;
	}
	
	/**
	 * Defines how to join tables references <code>rhs</code> and <code>lhs</code>.
	 *
	 * 
	 *   
	 * @param rhs
	 * @param lhs
	 * @param referencing
	 * @param krm
	 * @return
	 */
	protected <
		QT extends EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, QT>
	>	
	AbstractTableReference join(AbstractTableReference rhs, TableReference lhs, QT template, TableReference referencing, Map<ForeignKey, TableReference> krm) {		
		MultiForeignKeyJoinCondition mc = new MultiForeignKeyJoinCondition(referencing, krm);
		
		boolean inner = (krm.size() > 1 || template.getTemplateCount() > 0);
		
		AbstractTableReference joined = inner ? rhs.innerJoin(lhs, mc) : rhs.leftJoin(lhs, mc);
		
		return joined;
	}
	
	

	private	<
		KA extends Attribute,
		KR extends Reference,
		KT extends ReferenceType<KA, KR, KT, KE, KH, KF, KM, KC>,
		KE extends Entity<KA, KR, KT, KE, KH, KF, KM, KC>,
		KH extends ReferenceHolder<KA, KR, KT, KE, KH, KM, KC>,
		KF extends EntityFactory<KE, KH, KM, KF, KC>,
		KM extends EntityMetaData<KA, KR, KT, KE, KH, KF, KM, KC>,
		KC extends Content,
		KQ extends EntityQueryTemplate<KA, KR, KT, KE, KH, KF, KM, KC, KQ>		
	>
	AbstractTableReference processReferences(
			KQ template, 
			AbstractTableReference qref, 
			TableReference tref, 
			DefaultTableExpression q, 
			Set<EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?>> visited, 
			Map<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>, TableReference> rm,
			Map<EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?>, Entry> rem)
		throws EntityRuntimeException, CyclicTemplateException {
		
		logger().debug("processReferences - enter");		

		KM meta = template.getMetaData();		
		Set<KR> rs = meta.relationships();
		
		for (KR kr : rs) {
			EntityKey<KA, KR, KT, KE, KH, KF, KM, KC, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(kr);
			EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> t = template.getTemplate(ek);
			
			logger().debug("ref-template for: " + ek + " => " + t);
			
			if (t != null) {
				ForeignKey fk = meta.getForeignKey(kr);

				if (fk == null) {
					throw new NullPointerException("can not find fk by relationship: " + kr);
				}
				
				qref = fromTemplate(template.self(), t.self(), qref, fk, tref, q, visited, rm, rem);
			}									
		}
		
		
//		Map<EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?>, List<ForeignKey>> tkmap = 
//			new HashMap<EntityQueryTemplate<?,?,?,?,?,?,?,?,?>, List<ForeignKey>>(); 
//
//		for (KR kr : rs) {
//			EntityKey<KA, KR, KT, KE, KH, KF, KM, KC, ?, ?, ?, ?, ?, ?, ?, ?, ?> ek = meta.getEntityKey(kr);
//			EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> t = template.getTemplate(ek);
//			
//			logger().debug("ref-template for: " + ek + " => " + t);
//			
//			if (t != null) {
//				ForeignKey fk = meta.getForeignKey(kr);
//
//				if (fk == null) {
//					throw new NullPointerException("can not find fk by relationship: " + kr);
//				}
//				
//				List<ForeignKey> klist = tkmap.get(t);
//				
//				if (klist == null) {
//					klist = new ArrayList<ForeignKey>();
//					tkmap.put(t, klist);
//				}
//				
//				klist.add(fk);
//			}									
//		}
		
//		for (KR kr : tkmap) {
//			
//		}
//		
//		qref = fromTemplate(t.self(), qref, fk, tref, q, visited);

		logger().debug("processReferences - exit");
		
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
		D extends Entity<MA, ?, ?, D, ?, ?, DM, ?>,
		DM extends EntityMetaData<MA, ?, ?, D, ?, ?, DM, ?>,
		DQ extends EntityQueryTemplate<MA, ?, ?, D, ?, ?, DM, ?, DQ>
	> 
	void addAttributes(DQ template, Select s, TableReference tref) throws EntityRuntimeException {
		DM meta = template.getMetaData();
		Set<MA> as = meta.attributes();
		BaseTable t = meta.getBaseTable();
								
		for (MA a : as) {
			EntityQueryTemplateAttribute ta = template.get(a);
			
			if (ta == null) {
				continue;
			}
			
			Column c = meta.getColumn(a);
			
			if (t.isPrimaryKeyColumn(c)) {
				continue;
			}			
			
			ColumnReference cref = new ColumnReference(tref, c);
			
			getColumnMap().put(ta, cref);
			
			if (ta.isSelected(cref)) {
				s.add(cref);
			}
			
			Predicate p = ta.createPredicate(cref);			
			addPredicate(p);
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
	public EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?> getMetaData(TableReference tref) 
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

	private Map<TableReference, EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?>> getMetaDataMap() {
		if (metaDataMap == null) {
			metaDataMap = new HashMap<TableReference, EntityMetaData<?,?,?,?,?,?,?,?>>();			
		}

		return metaDataMap;
	}
		
	@Override
	public TableReference getReferenced(TableReference referencing, ForeignKey fk) {
		if (referencing == null) {
			return null;
		}
		
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
	
	public Logger logger() {
		return DefaultLogger.getLogger();
	}
}

