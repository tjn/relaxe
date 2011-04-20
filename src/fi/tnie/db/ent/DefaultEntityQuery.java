/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.expr.AbstractTableReference;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.ForeignKeyJoinCondition;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public class DefaultEntityQuery<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
	> implements EntityQuery<A, R, T, E>
{
	private EntityMetaData<A, R, T, E> meta;
	private DefaultTableExpression query;
	private TableReference rootRef;
	
	private Map<TableReference, EntityMetaData<?, ?, ?, ?>> metaDataMap;
	
	private LinkedHashMap<Integer, TableReference> originMap = new LinkedHashMap<Integer, TableReference>();	
	private Map<JoinKey, TableReference> referenceMap = new HashMap<JoinKey, TableReference>();
	
	
			
//	private static Logger logger = Logger.getLogger(DefaultEntityQuery.class.get);		
	
	public DefaultEntityQuery(EntityMetaData<A, R, T, E> meta) {		
		try {
			init(createPrototype(meta));
		} 
		catch (CyclicTemplateException e) {
			// can't happen - prototype does not have any non-null references.			
//			logger().error(e.getMessage(), e);
		}		
	}

	private E createPrototype(EntityMetaData<A, R, T, E> meta) {
		E p = meta.getFactory().newInstance();
		
		for (A a : meta.attributes()) {
			PrimitiveKey<A, R, T, E, ?, ?, ?, ?> k = meta.getKey(a);
			k.clear(p);
		}
		
		for (R a : meta.relationships()) {
			EntityKey<A, R, T, E, ?, ?, ?, ?> k = meta.getEntityKey(a);
			k.clear(p);
		}		
		
		return p;
	}	

	public DefaultEntityQuery(E root)
		throws CyclicTemplateException {
		super();
		init(root);
	}

	private void init(E root) throws CyclicTemplateException {
		if (root == null) {
			throw new NullPointerException();
		}

		EntityMetaData<A, R, T, E> meta = root.getMetaData();
		BaseTable table = meta.getBaseTable();

		if (table == null) {
			throw new NullPointerException("EntityMetaData.getBaseTable()");
		}

		this.meta = meta;

		DefaultTableExpression q = new DefaultTableExpression();
		HashSet<Entity<?,?,?,?>> visited = new HashSet<Entity<?,?,?,?>>();

		AbstractTableReference tref = fromTemplate(root, null, null, null, q, visited);
		q.setFrom(new From(tref));

		this.query = q;
	}


	private 
	<
		MA extends Attribute,
		MR,
		M extends Entity<MA, MR, ?, M>>
	AbstractTableReference fromTemplate(
			M template, 
			AbstractTableReference qref, ForeignKey fk, TableReference referencing, 
			DefaultTableExpression q, Set<Entity<?,?,?,?>> visited)
		throws CyclicTemplateException {
		
		if (visited.contains(template)) {
			throw new CyclicTemplateException(template);
		}
		else {
			visited.add(template);
		}		
		
		Select s = getSelect(q);
		EntityMetaData<MA, MR, ?, M> meta = template.getMetaData();				
		final TableReference tref = (qref == null) ? getTableRef() : new TableReference(meta.getBaseTable());
				
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
			s.add(new ColumnReference(tref, c));
		}
						
		Set<MR> rs = meta.relationships();

		// There are three cases:
		// 1) if the reference value does not exist, skip the relationship
		// 2) if the reference value exists, but is null, add the foreign key columns, but do not traverse
		// 3) if there is reference is not null, traverse

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
						
		for (MR r : rs) {
			EntityKey<?, ?, ?, M, ?, ?, ?, ?> k = meta.getEntityKey(r);			
			ReferenceHolder<?, ?, ?, ?> h = k.get(template);
		
			if (h == null) {
				// skip
			}
			else {
				Entity<?, ?, ?, ?> ne = h.value();
								
				if (ne == null) {
					// do not traverse
				}
				else {
					fk = meta.getForeignKey(r);
	
					if (fk == null) {
						throw new NullPointerException("can not find fk by relationship: " + r);
					}
	
					qref = fromTemplate(ne.self(), qref, fk, tref, q, visited);
				}
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
		M extends Entity<MA, ?, ?, M>
	> 
	void addAttributes(M template, Select s, TableReference tref) {
		EntityMetaData<MA, ?, ?, M> meta = template.getMetaData();
		Set<MA> as = meta.attributes();
		
		for (MA a : as) {
			PrimitiveHolder<?, ?> h = template.value(a);
						
			if (h != null) {
				Column c = meta.getColumn(a);
				
				// primary column are added separately:				
				if (c != null && c.isPrimaryKeyColumn() == false) {
					s.add(new ColumnReference(tref, c));
				}
			}
		}
	}

	public DefaultTableExpression getQuery() {
		return this.query;
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

	@Override
	public Long getLimit() {
		return null;
	}

	@Override
	public int getOffset() {
		return 0;
	}

	@Override
	public EntityMetaData<A, R, T, E> getMetaData() {
		return this.meta;
	}

	@Override
	public EntityMetaData<?, ?, ?, ?> getMetaData(TableReference tref) {
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

	private Map<TableReference, EntityMetaData<?, ?, ?, ?>> getMetaDataMap() {
		if (metaDataMap == null) {
			metaDataMap = new HashMap<TableReference, EntityMetaData<?,?,?,?>>();			
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

}
