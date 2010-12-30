/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;


/**
 * Value represents a value of monitorable value.
 * 
 * @author tnie
 *
 * @param <V> Type of the value.
 */
public interface ValueModel<V> {	
	/** 
	 * Current value of the model.
	 * 
	 * @return
	 */
	V get();
	
	/**
	 * Registers listener for value changes.   
	 * @param ch
	 * @return
	 */
	Registration addChangeHandler(ChangeListener<V> ch);	
}
