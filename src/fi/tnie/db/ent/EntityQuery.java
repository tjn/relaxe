/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.Request;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.types.ReferenceType;

public interface EntityQuery<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
>
	extends Request
{
	int getOffset();
	Long getLimit();

	public DefaultTableExpression getQuery();

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
	
	public TableReference getOrigin(int column);
	public EntityMetaData<?, ?, ?, ?, ?, ?, ?> getMetaData(TableReference tr);
	public TableReference getReferenced(TableReference referencing, ForeignKey fk);
	
}