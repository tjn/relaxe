/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;


import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.TimeHolder;

public interface HasTime<A extends Attribute, E extends HasTime<A, E>> {
	TimeHolder getTime(TimeKey<A, E> key);

	void setTime(TimeKey<A, E> key, TimeHolder newValue);
}
