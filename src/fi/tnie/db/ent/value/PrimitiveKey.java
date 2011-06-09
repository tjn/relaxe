/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.model.ent.EntityModel;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public interface PrimitiveKey<
	A extends Attribute,
	T extends ReferenceType<T, ?>,
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
	H get(E e);
	void set(E e, H newValue);
	void copy(E src, E dest);
	
	/**
	 * TODO: ?
	 * 
	 * @param m
	 * @return
	 */
	public ValueModel<H> getAttributeModel(EntityModel<A, T, E, ?> m);
}
