/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;

public interface HasLongVarBinaryKey<
	A extends Attribute,
	E extends HasLongVarBinary<A, E>	
>	{

	LongVarBinaryKey<A, E> getLongVarBinaryKey(A a);	
	void register(LongVarBinaryKey<A, E> key);
}
