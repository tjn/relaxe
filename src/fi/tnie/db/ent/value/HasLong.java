/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.LongHolder;

public interface HasLong<
	A extends Attribute,
	E extends HasLong<A, E>
>	
{	
	LongHolder getLong(LongKey<A, E> key);
	void setLong(LongKey<A, E> key, LongHolder newValue);	
}
