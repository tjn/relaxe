/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasDateKey<
	A extends Attribute,
	E extends HasDate<A, E>	
>	{

	DateKey<A, E> getDateKey(A a);	
	void register(DateKey<A, E> key);
}
