/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Set;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;

public abstract class AbstractEntityMetaData<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	M extends AbstractEntityMetaData<A, R, Q, M>
> 
	implements EntityMetaData<A, R, Q>
{
	public abstract Class<A> getAttributeNameType();
	public abstract Class<R> getRelationshipNameType();	
	public abstract Class<Q> getQueryNameType();
	
	public abstract BaseTable getBaseTable();
	
	public abstract Column getColumn(A a);
	public abstract A getAttribute(Column c);
	
	public abstract Set<A> getPKDefinition();	
}
