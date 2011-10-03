/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public interface NotNullableModel<V>
	extends MutableValueModel<V> {

	
	/**
	 * @throws NullPointerException If newValue is <code>null</code>.
	 */
	public void set(V newValue)
		throws NullPointerException;
	
	/**
	 * Returns <code>true</code>.
	 * @return
	 */
	boolean isNullable();
}
