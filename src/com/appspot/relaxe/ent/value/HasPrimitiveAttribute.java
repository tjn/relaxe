/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.io.Serializable;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.HasKey;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


public interface HasPrimitiveAttribute<
	A extends Attribute,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P, H>,	
	K extends PrimitiveKey<A, E, V, P, H, K>,
	E extends HasPrimitiveAttribute<A, V, P, H, K, E>
	>
	extends HasKey<P, K, E>
{
	
}
