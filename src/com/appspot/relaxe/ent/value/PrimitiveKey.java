/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.io.Serializable;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.AbstractPrimitiveType;


public interface PrimitiveKey<
	A extends Attribute,	
	E,
	V extends Serializable,
	P extends AbstractPrimitiveType<P>,
	H extends AbstractPrimitiveHolder<V, P, H>,	
	K extends PrimitiveKey<A, E, V, P, H, K>
>
	extends Key<E, P, K>, Serializable
{
	@Override
	P type();
	A name();
	H newHolder(V newValue);
	H get(E e) throws EntityRuntimeException;
	void set(E e, H newValue) throws EntityRuntimeException;	
	@Override
	void copy(E src, E dest) throws EntityRuntimeException;
	
	
	/**
	 * Assign the default value (the value of the expression <code>newHolder(null)</code>) for the attribute addressed by this key.
	 *  
	 * @param dest
	 */
	@Override
	void reset(E dest) throws EntityRuntimeException;
		
	@Override
	K self();
		
	/**
	 * If the type of the given holder is the same type type of this key, returns holder as a holder of type <code>H</code>. 
	 * Otherwise, returns <code>null</code>. 
	 *  
	 * @param holder
	 * 
	 * @throws NullPointerException if <code>holder</code> is <code>null</code>.
	 */
	H as(AbstractPrimitiveHolder<?, ?, ?> holder);

}
