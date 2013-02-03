/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;

public interface HasVarcharKey<
	A extends Attribute,
	E extends HasVarchar<A, E> & HasString<A, E>	
>	{

	VarcharKey<A, E> getVarcharKey(A a);	
	void register(VarcharKey<A, E> key);
}
