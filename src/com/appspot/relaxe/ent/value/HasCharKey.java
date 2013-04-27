/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasCharKey<
	A extends Attribute,
	E extends HasChar<A, E> & HasString<A, E>	
>	{

	CharKey<A, E> getCharKey(A a);	
	void register(CharKey<A, E> key);
}
