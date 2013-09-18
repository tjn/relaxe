/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.util.Set;

import com.appspot.relaxe.ent.im.EntityIdentityMap;
import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public interface EntityMetaData<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,	
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,	 
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends com.appspot.relaxe.ent.Content
> 		 
{

	/**
	 * Returns the base table this meta-data is bound to or <code>null</code>
	 * if this meta-data instance is not bound to any table.
	 *
	 * @return  The base table
	 */
	BaseTable getBaseTable();

	F getFactory();
	
	EntityBuilder<E, H> newBuilder(TableReference referencing, ForeignKey referencedBy, TableReference tableRef, EntityBuildContext ctx, UnificationContext unificationContext)
		throws EntityException;
	
	EntityIdentityMap<A, R, T, E, H, M> getIdentityMap(UnificationContext ctx);

	/**
	 * Unmodifiable set containing the names of the attributes which are applicable to entities this object represents.
	 * @return
	 */
	Set<A> attributes();
	Set<R> relationships();

	Column getColumn(A a);
	A getAttribute(Column c);
		
	
	PrimitiveKey<A, E, ?, ?, ?, ?> getKey(A a);
		
	EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> getEntityKey(R ref);

	ForeignKey getForeignKey(R r);

	/**
	 * Returns a set of the references the column <code>c</code> is part of.
	 *
	 * @param c
	 * @return
	 */
	Set<R> getReferences(Column c);

	T type();

	M self();
}
