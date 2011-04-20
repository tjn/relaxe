/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.types.ReferenceType;

public interface EntityQuery<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
{
//	QueryPredicate getPredicate();
	int getOffset();
	Long getLimit();

	public DefaultTableExpression getQuery();

	/**
	 * result item meta-data
	 * @return
	 */
	EntityMetaData<A, R, T, E> getMetaData();
	
	/**
	 * 
	 * @param column
	 * @return
	 */	
	public TableReference getOrigin(int column);
	public EntityMetaData<?, ?, ?, ?> getMetaData(TableReference tr);
	public TableReference getReferenced(TableReference referencing, ForeignKey fk);
	
}