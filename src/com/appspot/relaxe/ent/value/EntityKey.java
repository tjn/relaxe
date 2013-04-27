/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


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
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	RA extends Attribute,
	RR extends Reference,	
	RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM, RC>,
	RE extends Entity<RA, RR, RT, RE, RH, RF, RM, RC>,
	RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM, RC>,
	RF extends EntityFactory<RE, RH, RM, RF, RC>,
	RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM, RC>,
	RC extends Content,
	K extends EntityKey<A, R, T, E, H, F, M, C, RA, RR, RT, RE, RH, RF, RM, RC, K>	
>
	extends Key<E, RT, K>
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
	K self();
	
}
