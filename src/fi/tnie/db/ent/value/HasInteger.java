/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.IntegerHolder;

public interface HasInteger<
	A extends Attribute,
	E extends HasInteger<A, E>
>	
{
	/**
	 * Returns the value by the key or <code>null</code> if the value is not currently present.
	 * 
	 * @param key
	 * @return The value corresponding the key.
	 * @throws NullPointerException If <code>key</code> is <code>null</code>.
	 */
	IntegerHolder getInteger(IntegerKey<A, E> key);
	
	/**
	 * Sets the value by the key.
	 * 
	 * @param key
	 * @param newValue May be null.
	 * @throws NullPointerException If <code>key</code> is <code>null</code>.
	 */
	void setInteger(IntegerKey<A, E> key, IntegerHolder newValue);	
}
