/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.io.Serializable;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.HasKey;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.AbstractPrimitiveType;


public interface HasPrimitiveAttribute<
	A extends Attribute,
	V extends Serializable,
	P extends AbstractPrimitiveType<P>,
	H extends AbstractPrimitiveHolder<V, P, H>,	
	K extends PrimitiveKey<A, E, V, P, H, K>,
	E extends HasPrimitiveAttribute<A, V, P, H, K, E>
	>
	extends HasKey<P, K, E>
{
	
}
