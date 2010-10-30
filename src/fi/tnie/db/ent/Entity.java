/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.Map;

import fi.tnie.db.rpc.Holder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.meta.Column;

public interface Entity<
	A extends Enum<A> & Identifiable,
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, ? extends E>
> 
	extends Serializable
{		
	
	/**
	 * Returns a value of the attribute <code>a</code>  
	 * @param r
	 * @return
	 */
	Holder<?, ?> get(A a);
	
	/***
	 * Returns the value of the corresponding column.
	 * 
	 * If the <code>column</code> directly corresponds an attribute, the value of that attribute is returned.  
	 * Otherwise, the foreign keys the <code>column</code> is a part of are searched to find an entity reference.
	 * 
	 * If there is no referenced entity, <code>null</code> is returned.
	 * 
	 * If the entity reference <code>ref</code> (referenced by foreign key <code>F</code>) is found, 
	 * <code>column</code> is mapped to the corresponding column <code>fkcol</code> 
	 * in referenced table and result of the expression <code>ref.get(fkcol)</code> is returned.
	 *       
	 * @param column
	 * @return Scalar value or <code>null</code>, if the value is not set
	 * @throws NullPointerException If <code>c</code> is <code>null</code>.	 
	 */
	Holder<?, ?> get(Column c);
	
	/**
	 * Set the value of the attribute <code>a</code>
	 * 
	 * @param a
	 * @param value
	 */
	void set(A a, Holder<?, ?> value);
	
	/**
	 * Returns an entity to which this entity refers by relationship <code>r</code>  
	 * @param r
	 * @return
	 */
	Entity<?,?,?,?,?> get(R r);
	
	/**
	 * Sets an entity to which this entity refers by relationship <code>r</code>
	 * @param r
	 * @param ref
	 */
	void set(R r, Entity<?,?,?,?,?> ref);
	
	Map<Column, Holder<?, ?>> getPrimaryKey();
	
	/**
	 * Returns the meta-data object which describes the structure of this object.
	 * @return
	 */	
	EntityMetaData<A, R, Q, T, E> getMetaData();
	
	
	T getType();
}
