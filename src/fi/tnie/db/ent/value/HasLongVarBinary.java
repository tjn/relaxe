/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.LongVarBinaryHolder;

public interface HasLongVarBinary<
	A extends Attribute,
	E extends HasLongVarBinary<A, E>
>	
{
	/**
	 * Returns the value by the key or <code>null</code> if the value is not currently present.
	 * 
	 * @param key
	 * @return The value corresponding the key.
	 * @throws NullPointerException If <code>key</code> is <code>null</code>.
	 */
	LongVarBinaryHolder getLongVarBinary(LongVarBinaryKey<A, E> key);
	
	/**
	 * Sets the value by the key.
	 * 
	 * @param key
	 * @param newValue May be null.
	 * @throws NullPointerException If <code>key</code> is <code>null</code>.
	 */
	void setLongVarBinary(LongVarBinaryKey<A, E> key, LongVarBinaryHolder newValue);	
}
