/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasDoubleKey<A extends Attribute, E extends HasDouble<A, E>> {

	DoubleKey<A, E> getDoubleKey(A a);
	void register(DoubleKey<A, E> key);
}
