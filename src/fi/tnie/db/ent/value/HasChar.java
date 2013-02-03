/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.CharHolder;

public interface HasChar<
	A extends Attribute,
	E extends HasChar<A, E> & HasString<A, E>
>
{	
	CharHolder getChar(CharKey<A, E> key);
	void setChar(CharKey<A, E> key, CharHolder newValue);	
}
