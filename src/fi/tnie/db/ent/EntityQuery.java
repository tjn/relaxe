/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.types.ReferenceType;

public interface EntityQuery<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable, 
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, ? extends E>
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
	EntityMetaData<A, R, Q, T, ? extends E> getMetaData();
}