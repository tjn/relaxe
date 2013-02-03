/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;

public interface HasIntegerKey<
	A extends Attribute,
	E extends HasInteger<A, E>	
>	{

	IntegerKey<A, E> getIntegerKey(A a);	
	void register(IntegerKey<A, E> key);
}
