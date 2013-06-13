/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.service;

import java.util.List;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.EntityQueryTemplate;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public interface EntitySession {
	
	<
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content
	>
	E merge(E e) throws EntityException;
	
	<
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content
	>
	E insert(E e)
		throws EntityException;
	
	
	<
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content
	>
	E update(E e)
		throws EntityException;	
	
	<
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content
	>
	void delete(E e)
		throws EntityException;

	
	<
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content
	>
	E sync(E e) throws EntityException;	
	
	public <
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content,
		QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>
	>
	EntityQueryResult<A, R, T, E, H, F, M, C, QT> query(EntityQuery<A, R, T, E, H, F, M, C, QT> query, FetchOptions opts)
		throws EntityException;
	
	
	public <
		A extends Attribute,
		R extends Reference,	
		T extends ReferenceType<A, R, T, E, H, F, M, C>,
		E extends Entity<A, R, T, E, H, F, M, C>,
		H extends ReferenceHolder<A, R, T, E, H, M, C>,
		F extends EntityFactory<E, H, M, F, C>,		
		M extends EntityMetaData<A, R, T, E, H, F, M, C>,
		C extends com.appspot.relaxe.ent.Content,
		QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>
	>
	List<E> load(QT qt, FetchOptions opts)
		throws EntityException;
	
	/**
	 * 
	 */
	void flush();
	


	public <		
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, H, F, M, ?>,
		E extends Entity<A, R, T, E, H, F, M, ?>,
		H extends ReferenceHolder<A, R, T, E, H, M, ?>,
		F extends EntityFactory<E, H, M, F, ?>,
		M extends EntityMetaData<A, R, T, E, H, F, M, ?>		
	>
	E newEntity(T type);
}
