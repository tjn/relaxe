/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasVarcharArrayKey<
	A extends Attribute,
	E extends HasVarcharArray<A, E>	
>	{

	VarcharArrayKey<A, E> getVarcharArrayKey(A a);	
	void register(VarcharArrayKey<A, E> key);
}
