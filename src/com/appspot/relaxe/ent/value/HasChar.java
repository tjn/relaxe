/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.CharHolder;

public interface HasChar<
	A extends Attribute,
	E extends HasChar<A, E> & HasString<A, E>
>
{	
	CharHolder getChar(CharKey<A, E> key);
	void setChar(CharKey<A, E> key, CharHolder newValue);	
}
