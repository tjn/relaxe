/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;

public interface HasDecimalKey<A extends Attribute, E extends HasDecimal<A, E>> {

	DecimalKey<A, E> getDecimalKey(A a);
	void register(DecimalKey<A, E> key);
}
