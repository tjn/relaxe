/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasTimeKey<A extends Attribute, E extends HasTime<A, E>> {

	TimeKey<A, E> getTimeKey(A a);

	void register(TimeKey<A, E> key);
}
