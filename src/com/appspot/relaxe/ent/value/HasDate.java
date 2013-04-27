/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.DateHolder;

public interface HasDate<
	A extends Attribute,
	E extends HasDate<A, E>
>	
{	
	DateHolder getDate(DateKey<A, E> key);
	void setDate(DateKey<A, E> key, DateHolder newValue);	
}
