/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.Predicate;
import fi.tnie.db.expr.TableColumnExpr;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.expr.ValueExpression;
import fi.tnie.db.expr.ValueParameter;
import fi.tnie.db.expr.op.AndPredicate;
import fi.tnie.db.expr.op.Eq;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

/**
 *	TODO: 
		This does not handle well enough the case
		of overlapping foreign-keys:
		If the foreign key A "contains" another foreign key B (
		the set of columns in a foreign key A contains
		the columns of another foreign key B as a proper subset),
		we could assume the table <code>T</code> <code>A</code> references also contains
		a foreign key <code>C</code> which also references table <code>T</code>.

		Proper implementation should probably set conflicting references to <code>null</code>.
 
 * @author Administrator
 *
 * @param <A>
 * @param <R>
 * @param <Q>
 * @param <E>
 */


public abstract class DefaultEntity<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>
>
	extends AbstractEntity<A, R, Q, E> {
	
	private EnumMap<A, Object> values;
	private ReferenceMap<R, Entity<?,?,?,?>> refs;
	
	private EntityQueryResult<A, R, Q, ?> result;
		
	private static Logger logger = Logger.getLogger(DefaultEntity.class);
	
	protected DefaultEntity() {
		super();		
	}

	public Object get(A a) {
		if (a == null) {
			throw new NullPointerException("'a' must not be null");
		}
		
		return attrs().get(a);		
	};

	public Object get(Column column)
		throws NullPointerException {
		if (column == null) {
			throw new NullPointerException("'c' must not be null");
		}
		
		EntityMetaData<A, R, Q, E> m = getMetaData();
		
		A a = m.getAttribute(column);
		
		if (a != null) {
			return get(a);			
		}
		
		// column may be part of multiple
		// overlapping foreign-keys:				
		Set<R> rs = m.getReferences(column);
		
		if (rs == null) {
			return null;
		}
	
		Entity<?, ?, ?, ?> ref = null;
		R r = null;
		
		for (R ri : rs) {
			ref = this.get(r);
			
			if (ref != null) {				
				r = ri;
				break;
			}
		}
				
		if (ref == null) {			
			return null;
		}
		
		ForeignKey fk = m.getForeignKey(r);
		Column fkcol = fk.columns().get(column);		
		return ref.get(fkcol);
	};
	
	public void set(A a, Object value) {
		attrs().put(a, value);		
	}
	
	protected EnumMap<A, Object> attrs() {
		if (values == null) {
			values = new EnumMap<A, Object>(getMetaData().getAttributeNameType()); 
		}
		return values;
	}
	
	protected ReferenceMap<R, Entity<?,?,?,?>> refs() {
		if (refs == null) {
			refs = new ReferenceMap<R, Entity<?,?,?,?>>(getMetaData().getRelationshipNameType()); 
		}
		
		return refs;
	}

	public void set(R r, fi.tnie.db.Entity<?,?,?,?> ref) {	
		refs().put(r, ref);
	}
	
	public Entity<?,?,?,?> get(R r) {
		return refs().get(r);
	}	

	// TODO EntityDiff<E> & MergeStrategy ms  
	public void store() {			
		
	}
	
//	public void insert(Connection c) 
//		throws EntityException {
//		InsertStatement q = createInsertStatement();
//		String qs = q.generate();
//		
//		PreparedStatement ps = null;
//		ResultSet rs = null;		 
//		
//		try {		
//			logger().debug("JDBC-driver :" + c.getMetaData().getDriverName());
//			logger().debug("JDBC-driver-maj:" + c.getMetaData().getDriverMajorVersion());
//			logger().debug("JDBC-driver-min:" + c.getMetaData().getDriverMinorVersion());
//			logger().debug("JDBC-support-get-gen:" + c.getMetaData().supportsGetGeneratedKeys());
//			
//			ps = c.prepareStatement(qs, Statement.RETURN_GENERATED_KEYS);
//			q.traverse(null, new ParameterAssignment(ps));
//			
//			logger().debug("q: " + qs);
//			int ins = ps.executeUpdate();	
//			logger().debug("inserted: " + ins);
//			
//			rs = ps.getGeneratedKeys();
//			
////			logger().debug(buf.toString());
//			
//			if (rs.next()) {
////				buf.setLength(0);
//				
//					int cc = rs.getMetaData().getColumnCount();
//	//			StringBuffer buf = new StringBuffer();			
//				
//				EntityMetaData<A, R, ?, E> em = getMetaData();
//				BaseTable table = em.getBaseTable();
//				ColumnMap cm = table.columnMap();
//				List<A> keys = new ArrayList<A>();
//							
//				for (int i = 1; i <= cc; i++) {
//					String name = rs.getMetaData().getColumnLabel(i);
//					Column col = cm.get(name);
//					
//					if (col == null) {
//						throw new NullPointerException("Can not find column " + name + " in table: " + table.getQualifiedName());
//					} 
//					
//					A attr = em.getAttribute(col);				
//					keys.add(attr);				
//					
//	//				buf.append(name);
//	//				buf.append("\t");
//				}			
//								
//				for (int i = 1; i <= cc; i++) {					
//					A attr = keys.get(i - 1);
//					
//					if (attr != null) {
//						Object value = rs.getObject(i);
//						set(attr, value);
//					}
//															
////					String col = rs.getString(i);
////					buf.append(col);
////					buf.append("\t");
//				}
//			}
//				
////				logger().debug(buf.toString());				
//		}
//		catch (SQLException e) {
//			logger().error(e.getMessage(), e);
//			throw new EntityException(e.getMessage(), e);
//		}
//		finally {
//			rs = QueryHelper.doClose(rs);			
//			ps = QueryHelper.doClose(ps);
//		}
//	}	

	public static Logger logger() {
		return DefaultEntity.logger;
	}
		
	protected Predicate getPKPredicate(TableReference tref) 
		throws EntityException {
		
		if (tref == null) {
			throw new NullPointerException();
		}
		
		EntityMetaData<A, R, Q, E> meta = getMetaData();
		Set<Column> pkcols = meta.getPKDefinition();
				
		if (pkcols.isEmpty()) {
			throw new EntityException("no pk-attributes available");			
		}					
		
		Predicate p = null;
		
		for (Column col : pkcols) {
			Object o = get(col);
			
			// to successfully create a pk predicate 
			// every component must be set: 			
			if (o == null) {				
				return null;
			}
			
			ColumnExpr ce = new TableColumnExpr(tref, col);
			ValueParameter param = new ValueParameter(col, o);
			p = AndPredicate.newAnd(p, eq(ce, param));
		}
		
		return p;
	}
	
	private Eq eq(ValueExpression a, ValueExpression b) {
		return new Eq(a, b);
	}
	
	protected TableReference createTableRef() {
		return new TableReference(getMetaData().getBaseTable());
	}	
	
	
	public EntityDiff<A, R, Q, E> diff(E another) {
		final E self = self();
				
		if (this == another || another == null) {
			return new EmptyEntityDiff<A, R, Q, E>(self);
		}
		
		return new EntitySnapshotDiff<A, R, Q, E>(self, another);
	}

	/**
	 * Returns a type-safe self-reference. Implementation must return <code>this</code>.
	 *  
	 * @return
	 */
	protected abstract E self();
	
	public Map<Column, Object> getPrimaryKey() {
		Map<Column, Object> pk = new HashMap<Column, Object>(); 
		
		for (Column pkcol : getMetaData().getPKDefinition()) {
			Object v = get(pkcol);
			
			if (v == null) {
				return null;
			}
			
			pk.put(pkcol, v);
		}
		
		return pk;
	}	
	
	void markLoaded(EntityQueryResult<A, R, Q, ?> result) {
		this.result = result;
	}
}