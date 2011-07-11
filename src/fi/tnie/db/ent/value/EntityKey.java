/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.model.ent.EntityModel;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;


/**
 * Key to address an entity reference of the type <code>V</code> within an entity of type <code>E</code> 
 * 
 * @author tnie
 *
 * @param <R>
 * @param <T>
 * @param <E>
 * @param <S>
 * @param <P>
 * @param <V>
 * @param <H>
 * @param <D>
 * @param <K>
 */
public interface EntityKey<	
	R extends Reference,
	T extends ReferenceType<T, S>,
	E extends Entity<?, R, T, E, ?, ?, S>,	
	S extends EntityMetaData<?, R, T, E, ?, ?, S>,	
	P extends ReferenceType<P, D>,
	V extends Entity<?, ?, P, V, H, ?, D>,
	H extends ReferenceHolder<?, ?, P, V, H, D>,
	D extends EntityMetaData<?, ?, P, V, H, ?, D>,	
	K extends EntityKey<R, T, E, S, P, V, H, D, K>	
>
	extends Key<T, E, P, K>, Serializable
{	
	R name();	
	H newHolder(V newValue);	
	H get(E e);
	V value(E e);
	void set(E e, H newValue);
	void set(E e, V newValue);
	
	S getSource();	
	D getTarget();
	
	<M extends EntityModel<?, R, T, E, ?, ?, S, M>>
	ValueModel<H> getReferenceModel(M m) throws EntityRuntimeException;
		
}
