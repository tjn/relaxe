/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public interface PrimitiveKey<
	A extends Attribute,	
	E,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P, H>,	
	K extends PrimitiveKey<A, E, V, P, H, K>
>
	extends Key<E, P, K>, Serializable
{
	P type();
	A name();
	H newHolder(V newValue);
	H get(E e) throws EntityRuntimeException;
	void set(E e, H newValue) throws EntityRuntimeException;	
	void copy(E src, E dest) throws EntityRuntimeException;
	
	
	/**
	 * Assign the default value (the value of the expression <code>newHolder(null)</code>) for the attribute addressed by this key.
	 *  
	 * @param dest
	 */
	void reset(E dest) throws EntityRuntimeException;
		
	K self();
		
	/**
	 * If the type of the given holder is the same type type of this key, returns holder as a holder of type <code>H</code>. 
	 * Otherwise, returns <code>null</code>. 
	 *  
	 * @param holder
	 * 
	 * @throws NullPointerException if <code>holder</code> is <code>null</code>.
	 */
	H as(PrimitiveHolder<?, ?, ?> holder);

}
