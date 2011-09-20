/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
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
 * @param <VE>
 * @param <VH>
 * @param <VM>
 * @param <K>
 */
public interface EntityKey<	
	R extends Reference,
	T extends ReferenceType<?, R, T, E, ?, ?, S>,
	E extends Entity<?, R, T, E, ?, ?, S>,	
	S extends EntityMetaData<?, R, T, E, ?, ?, S>,	
	P extends ReferenceType<VA, VR, P, VE, VH, VF, VM>,
	VA extends Attribute,
	VR extends Reference,	
	VE extends Entity<VA, VR, P, VE, VH, VF, VM>,
	VH extends ReferenceHolder<VA, VR, P, VE, VH, VM>,
	VF extends EntityFactory<VE, VH, VM, VF>,
	VM extends EntityMetaData<VA, VR, P, VE, VH, VF, VM>,	
	K extends EntityKey<R, T, E, S, P, VA, VR, VE, VH, VF, VM, K>	
>
	extends Key<T, E, P, K>, Serializable
{	
	R name();	
	VH newHolder(VE newValue);	
	VH get(E e);
	VE value(E e);
	void set(E e, VH newValue);
	void set(E e, VE newValue);
	
//	P getTargetType();
	S getSource();	
	VM getTarget();


}
