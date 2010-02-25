/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.SQLException;

import fi.tnie.db.expr.DeleteStatement;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.expr.UpdateStatement;
import fi.tnie.db.meta.Column;

public interface Entity<
	A extends Enum<A> & Identifiable,
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>
> {		
	
	/**
	 * Returns a value of the attribute <code>a</code>  
	 * @param r
	 * @return
	 */
	Object get(A a);
	
	/**
	 * Returns a value of the attribute corresponding the column <code>c</code>  
	 * @param c
	 * @return
	 */
	Object get(Column c);
	
	/**
	 * Set the value of the attribute <code>a</code>
	 * 
	 * @param a
	 * @param value
	 */
	void set(A a, Object value);
	
	/**
	 * Returns an entity to which this entity refers by relationship <code>r</code>  
	 * @param r
	 * @return
	 */
	Entity<?,?,?,?> get(R r);
	
	/**
	 * Sets an entity to which this entity refers by relationship <code>r</code>
	 * @param r
	 * @param ref
	 */
	void set(R r, Entity<?,?,?,?> ref);
	
	/**
	 * Returns the meta-data object which describes the structure of this object.
	 * @return
	 */	
	EntityMetaData<A, R, Q, E> getMetaData();
	
	void insert(Connection c)
		throws SQLException, EntityException;

	void update(Connection c)
		throws SQLException, EntityException;

	void delete(Connection c)
		throws SQLException, EntityException;
	
	InsertStatement createInsert();
	UpdateStatement createUpdateStatement()
		throws EntityException;
	DeleteStatement createDeleteStatement() 
		throws EntityException;
}
