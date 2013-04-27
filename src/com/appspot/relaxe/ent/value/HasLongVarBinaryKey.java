/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasLongVarBinaryKey<
	A extends Attribute,
	E extends HasLongVarBinary<A, E>	
>	{

	LongVarBinaryKey<A, E> getLongVarBinaryKey(A a);	
	void register(LongVarBinaryKey<A, E> key);
}
