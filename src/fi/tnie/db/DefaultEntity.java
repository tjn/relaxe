/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumMap;
import java.util.Map;

import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.ValueParameter;
import fi.tnie.db.expr.ValueRow;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.impl.ColumnMap;

public abstract class DefaultEntity<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, ? extends E>
>
	extends AbstractEntity<A, R, Q, E> {
	
	private EnumMap<A, Object> values;
	private EntityMap<R, Entity<?, ?, ?>> refs;
	
	protected DefaultEntity() {
		super();		
	}

	public Object get(A a) {
		return attrs().get(a);		
	};
	
	public void set(A a, Object value) {
		attrs().put(a, value);
	}
	
	private EnumMap<A, Object> attrs() {
		if (values == null) {
			values = new EnumMap<A, Object>(getMetaData().getAttributeNameType()); 
		}
		return values;
	}
	
//	private EnumMap<R, Entity<?, ?, ?>> refs() {
//		if (refs == null) {
//			refs = new EnumMap<R, Entity<?,?,?>>(getMetaData().getRelationshipNameType()); 
//		}
//		
//		return refs;
//	}

	private EntityMap<R, Entity<?,?,?>> refs() {
		if (refs == null) {
			refs = new EntityMap<R, Entity<?,?,?>>(getMetaData().getRelationshipNameType()); 
		}
		
		return refs;
	}

	public void set(R r, fi.tnie.db.Entity<?,?,?> ref) {
		refs().put(r, ref);
	}
	
	public Entity<?,?,?> get(R r) {
		return refs().get(r);
	}
	

	// TODO EntityDiff<E> & MergeStrategy ms  
	public void store() {
			
		
	}
	
	public void insert(Connection c) 
		throws SQLException {		
 
	InsertStatement q = createInsertQuery();		
	String qs = q.generate();		
	final PreparedStatement ps = c.prepareStatement(qs, Statement.RETURN_GENERATED_KEYS);					
	q.traverse(null, new ParameterAssignment(ps));
		
	int ins = ps.executeUpdate();
	
//	logger().debug("inserted: " + ins);
	
	ResultSet rs = ps.getGeneratedKeys();
	
	try {
								
		// TODO: read generated keys:									
//		RowFactory<K, E> ef = getFactory();
//		EnumMap<K, Integer> keys = ef.columns(rs.getMetaData());					
//		
//		if (rs.next()) {
//			ef.copy(keys, rs, this);				
//		}
	}
	finally {
		rs.close();
	}
}


	public InsertStatement createInsertQuery() {
	
		ValueRow newRow = new ValueRow();
				
		BaseTable t = getMetaData().getBaseTable();
				
//		getMetaData().getForeignKey(r)
		
//		getMetaData().relationships()
		
//		ColumnMap cm = t.columnMap();
		ElementList<ColumnName> names = new ElementList<ColumnName>();
				
//		for (Map.Entry<C, ?> e : values.entrySet()) {			
//			C column = e.getKey();			
//			Column col = cm.get(column.identifier());
//			names.add(col.getColumnName());
//			ValueParameter p = new ValueParameter(col, e.getValue());
//			newRow.add(p);
//		}
				
		return new InsertStatement(t, names, newRow);
	}

}
