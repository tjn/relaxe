/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.query.QueryResult;
import fi.tnie.db.rpc.AbstractResponse;
import fi.tnie.db.types.ReferenceType;

public class DefaultEntityQueryResult<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, ?, ?, M>,	
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
>
	extends AbstractResponse<EntityQuery<A, R, T, E, M>>
	implements EntityQueryResult<A, R, T, E, M> {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6159979642605451277L;
	
	private QueryResult<EntityDataObject<E>> content;
	
	protected DefaultEntityQueryResult() {
		super();
	}

	public DefaultEntityQueryResult(EntityQuery<A, R, T, E, M> request, QueryResult<EntityDataObject<E>> content) {
		super(request);
		this.content = content;
	}

	public QueryResult<EntityDataObject<E>> getContent() {
		return this.content;
	}
}
