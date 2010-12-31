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
	 * @param ch
	 * @return
	 */
	Registration addChangeHandler(ChangeListener<V> ch);

	/**
	 * If this model is mutable, returns 
	 * a model by which this model can be manipulated (typically <code>this</code>)
	 * 
	 * Returns <code>null</code> if this model is read-only. 
	 *   
	 * @return
	 */
	MutableValueModel<V> asMutable();
	
	/**
	 * If this model is immutable, returns itself. 
	 * If this model is mutable, wraps itself in immutable model and returns the immmmutable view. 
	 *   
	 * @return
	 */	
	ImmutableValueModel<V> asImmutable();
}
