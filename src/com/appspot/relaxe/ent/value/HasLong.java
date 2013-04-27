/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.LongHolder;

public interface HasLong<
	A extends Attribute,
	E extends HasLong<A, E>
>	
{	
	LongHolder getLong(LongKey<A, E> key);
	void setLong(LongKey<A, E> key, LongHolder newValue);	
}
