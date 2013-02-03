/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.VarcharHolder;

public interface HasVarchar<
	A extends Attribute,
	E extends HasVarchar<A, E> & HasString<A, E>
>
{	
	VarcharHolder getVarchar(VarcharKey<A, E> key);
	void setVarchar(VarcharKey<A, E> key, VarcharHolder newValue);	
}
