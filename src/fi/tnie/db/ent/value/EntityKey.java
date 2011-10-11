/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

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
 * @param <M>
 * @param <RT>
 * @param <RE>
 * @param <RH>
 * @param <RM>
 * @param <K>
 */
public interface EntityKey<	
	R extends Reference,
	T extends ReferenceType<?, R, T, E, ?, ?, M>,
	E extends Entity<?, R, T, E, ?, ?, M>,	
	M extends EntityMetaData<?, R, T, E, ?, ?, M>,	
	RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM>,
	RA extends Attribute,
	RR extends Reference,	
	RE extends Entity<RA, RR, RT, RE, RH, RF, RM>,
	RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
	RF extends EntityFactory<RE, RH, RM, RF>,
	RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM>,	
	K extends EntityKey<R, T, E, M, RT, RA, RR, RE, RH, RF, RM, K>	
>
	extends Key<T, E, RT, K>
{	
	R name();	
	RH newHolder(RE newValue);	
	RH get(E e);
	RE value(E e);
	void set(E e, RH newValue);
	void set(E e, RE newValue);
	
//	P getTargetType();
	M getSource();	
	RM getTarget();


}
