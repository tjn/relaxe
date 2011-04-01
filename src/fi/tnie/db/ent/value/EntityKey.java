/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

/**
 * 
 * @author tnie
 *
 * @param <A> 
 * @param <R>
 * @param <T>
 * @param <E> The entity type containing the key. 
 * @param <V> Type of the referenced entity.
 * @param <P> Type of the entity to address with this key 
 * @param <H> Type of the holder for the addressed entity
 * @param <K> 
 */

public interface EntityKey<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>,	
	P extends ReferenceType<P>,
	V extends Entity<?, ?, P, V>,
	H extends ReferenceHolder<?, ?, P, V>,	
	K extends EntityKey<A, R, T, E, P, V, H, ? extends K>
>
	extends Key<A, R, T, E, P, K>, Serializable
{	
	R name();	
	H newHolder(V newValue);	
	H get(E e);
	V value(E e);
	void set(E e, H newValue);
	void set(E e, V newValue);	
}
