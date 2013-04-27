/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.DecimalHolder;

public interface HasDecimal<
	A extends Attribute, E extends HasDecimal<A, E>
> {
	DecimalHolder getDecimal(DecimalKey<A, E> key);
	void setDecimal(DecimalKey<A, E> key, DecimalHolder newValue);
}
