/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public interface EntityQuery<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,		
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, QT>
>
	extends Request, QueryExpressionSource	
{	
	DefaultTableExpression getTableExpression()
		throws EntityException;
	
	QueryExpression getQueryExpression()
		throws QueryException;

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
	
	QT getTemplate();	
}