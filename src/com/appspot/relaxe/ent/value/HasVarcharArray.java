/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.VarcharArrayHolder;

public interface HasVarcharArray<
	A extends Attribute,
	E extends HasVarcharArray<A, E>
>	
{	
	VarcharArrayHolder getVarcharArray(VarcharArrayKey<A, E> key);
	void setVarcharArray(VarcharArrayKey<A, E> key, VarcharArrayHolder newValue);	
}
