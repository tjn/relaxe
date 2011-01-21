/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.types.ReferenceType;

public interface EntityQuery<
	A extends Serializable,
	R,
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
}