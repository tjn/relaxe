/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.expr.DefaultTableExpression;
import com.appspot.relaxe.expr.QueryExpression;
import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.ForeignKey;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;

public interface EntityQuery<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,		
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>
>
	extends Request, QueryExpressionSource, EntityQueryContext
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
	
	public EntityMetaData<?, ?, ?, ?, ?, ?, ?, ?> getMetaData(TableReference tr)
		throws EntityException;
	
	public TableReference getReferenced(TableReference referencing, ForeignKey fk)
		throws EntityException;
	
	QT getTemplate();	
}