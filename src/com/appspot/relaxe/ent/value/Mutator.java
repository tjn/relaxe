/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.io.Serializable;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.AbstractPrimitiveType;


public interface Mutator<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?>,
	S extends Serializable,
	P extends AbstractPrimitiveType<P>,
	H extends AbstractPrimitiveHolder<S, P, H>,	
	K extends PrimitiveKey<A, E, S, P, H, K>
>
	extends PrimitiveAccessor<A, E, S, P, H, K>
{
	void set(S newValue)
		throws EntityException;
	void setHolder(H newHolder)
		throws EntityException;
}