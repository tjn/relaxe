/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasIntegerKey<
	A extends Attribute,
	E extends HasInteger<A, E>	
>	{

	IntegerKey<A, E> getIntegerKey(A a);	
	void register(IntegerKey<A, E> key);
}
