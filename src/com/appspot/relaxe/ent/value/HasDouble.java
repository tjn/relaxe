/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.DoubleHolder;

public interface HasDouble<A extends Attribute, E extends HasDouble<A, E>> {
	DoubleHolder getDouble(DoubleKey<A, E> key);
	void setDouble(DoubleKey<A, E> key, DoubleHolder newValue);
}
