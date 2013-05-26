/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasLongKey<
	A extends Attribute,
	E extends HasLong<A, E>	
>	{

	LongKey<A, E> getLongKey(A a);	
	void register(LongKey<A, E> key);
}