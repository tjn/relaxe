/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Set;

import fi.tnie.db.ent.im.EntityIdentityMap;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public interface EntityMetaData<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,	
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,	 
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends fi.tnie.db.ent.Content
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
