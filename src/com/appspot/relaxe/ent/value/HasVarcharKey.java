/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasVarcharKey<
	A extends Attribute,
	E extends HasVarchar<A, E> & HasString<A, E>	
>	{

	VarcharKey<A, E> getVarcharKey(A a);	
	void register(VarcharKey<A, E> key);
}
