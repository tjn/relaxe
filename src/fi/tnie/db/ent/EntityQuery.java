/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.types.ReferenceType;

public interface EntityQuery<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, ?, ?, M>,	
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
>
	extends Request, QueryExpressionSource	
{
	long getOffset();
	long getLimit();

	DefaultTableExpression getTableExpression()
		throws EntityException;
	
	QueryExpression getQueryExpression()
		throws EntityException;

	/**
	 * result item meta-data
	 * @return
	 */
	M getMetaData();
	
	TableReference getTableRef();
	
	
	/**
	 * 
	 * @param column
	 * @return
	 */
	
	public TableReference getOrigin(int column)
		throws EntityException;
	
	public EntityMetaData<?, ?, ?, ?, ?, ?, ?> getMetaData(TableReference tr)
		throws EntityException;
	
	public TableReference getReferenced(TableReference referencing, ForeignKey fk)
		throws EntityException;
	
}