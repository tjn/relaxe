/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;

public interface HasBooleanKey<
	A extends Attribute,
	E extends HasBoolean<A, E>	
>	{

	BooleanKey<A, E> getBooleanKey(A a);	
	void register(BooleanKey<A, E> key);
}
