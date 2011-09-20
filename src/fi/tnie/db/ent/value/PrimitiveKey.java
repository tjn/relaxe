/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public interface PrimitiveKey<
	A extends Attribute,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P>,	
	K extends PrimitiveKey<A, T, E, V, P, H, K>
>
	extends Key<T, E, P, K>, Serializable
{
	P type();
	A name();
	H newHolder(V newValue);
	H get(E e) throws EntityRuntimeException;
	void set(E e, H newValue) throws EntityRuntimeException;	
	void copy(E src, E dest) throws EntityRuntimeException;
		
	K self();
		
}
