/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public interface ValueModel<V> {	
	public V get();		
	Registration addChangeHandler(ChangeListener<V> ch);	
}
