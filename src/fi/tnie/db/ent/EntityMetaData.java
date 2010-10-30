/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Set;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.types.ReferenceType;

public interface EntityMetaData<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, ? extends E>
> {

	Class<A> getAttributeNameType();
	Class<R> getRelationshipNameType();	
	Class<Q> getQueryNameType();
	
	/**
	 * Returns the base table this meta-data is bound to or <code>null</code> 
	 * if this meta-data instance is not bound to any table.
	 *  
	 * @return  The base table
	 */
	BaseTable getBaseTable();
	
	EntityFactory<A, R, Q, T, E> getFactory();
	
	/**
	 * Unmodifiable set containing the names of the attributes which are applicable to entities this object describes.   
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
	Set<Column> getPKDefinition();
	
	void bind(BaseTable table)
		throws EntityException;
		
	/**
	 * Returns a set of the references the column <code>c</code> is part of.
	 *   
	 * @param c
	 * @return
	 */	
	Set<R> getReferences(Column c);
	
	/**
	 * Returns an object identical to getBaseTable().getSchema().getCatalog()
	 * @return
	 */	
	Catalog getCatalog();
	
	T getType();
}
