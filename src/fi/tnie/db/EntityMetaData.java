/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Set;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;

public interface EntityMetaData<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, ? extends E>
> {

	Class<A> getAttributeNameType();
	Class<R> getRelationshipNameType();	
	Class<Q> getQueryNameType();
	
	/**
	 * Returns the base table this meta-data is bound to.
	 * @return
	 */
	BaseTable getBaseTable();
	
	EntityFactory<A, R, Q, E> getFactory();
	
	/**
	 * Unmodifiable set containing the names of the attibutes which are applicable to entities this object describes.   
	 * @return
	 */
	Set<A> attributes();
	
	/**
	 * Unmodifiable set containing the names of the relationships which are applicable to entities this object describes.   
	 * @return
	 */	
	Set<R> relationships();
	
	Column getColumn(A a);
	A getAttribute(Column c);
	
	ForeignKey getForeignKey(R r);
	
	Set<A> getPKDefinition();

	
	void bind(BaseTable table)
		throws EntityException;
}
