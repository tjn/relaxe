/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public interface Entity<
	A extends Enum<A> & Identifiable,
	R extends Enum<R> & Identifiable	
> {		
	
	/**
	 * Returns a value of the attribute <code>a</code>  
	 * @param r
	 * @return
	 */
	Object get(A a);
	
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
	Entity<?, ?> get(R r);
	
	/**
	 * Sets an entity to which this entity refers by relationship <code>r</code>
	 * @param r
	 * @param ref
	 */
	void set(R r, Entity<?, ?> ref);
	
	/**
	 * Returns the meta-data object which describes the structure of this.
	 * @return
	 */
	
	EntityMetaData<A, R, ?> getMetaData();
}
