/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.VarcharHolder;

public interface HasVarchar<
	A extends Attribute,
	E extends HasVarchar<A, E> & HasString<A, E>
>
{	
	VarcharHolder getVarchar(VarcharKey<A, E> key);
	void setVarchar(VarcharKey<A, E> key, VarcharHolder newValue);	
}
