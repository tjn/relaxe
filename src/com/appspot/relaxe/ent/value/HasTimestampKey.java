/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasTimestampKey<A extends Attribute, E extends HasTimestamp<A, E>> {

	TimestampKey<A, E> getTimestampKey(A a);

	void register(TimestampKey<A, E> key);
}
