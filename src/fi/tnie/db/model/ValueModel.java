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
	 * Registers a listener for value changes.
	 *    
	 * @param ch
	 * @return
	 */
	Registration addChangeHandler(ChangeListener<V> ch);

	/**
	 * If this model is mutable, returns model by which this model can be manipulated (typically <code>this</code>)
	 * 
	 * Returns <code>null</code> if this model is read-only. 
	 *   
	 * @return
	 */
	MutableValueModel<V> asMutable();
	
	/**
	 * Returns true if and only if this model is mutable. 
	 *   
	 * @return
	 */	
	boolean isMutable();
	
	/**
	 * If this model is immutable, returns itself, otherwise, wraps itself in immutable model and returns the immutable view. 
	 *   
	 * @return
	 */	
	ImmutableValueModel<V> asImmutable();
}
