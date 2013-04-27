/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;


import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.TimestampHolder;

public interface HasTimestamp<A extends Attribute, E extends HasTimestamp<A, E>> {
	TimestampHolder getTimestamp(TimestampKey<A, E> key);

	void setTimestamp(TimestampKey<A, E> key, TimestampHolder newValue);
}
