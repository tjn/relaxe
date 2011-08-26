/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.ent.im.AbstractResponse;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityQueryResult <
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
>
	extends AbstractResponse<EntityQuery<A, R, T, E, M>>
	implements EntityQueryResult<A, R, T, E, M>
	 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4702982201316758878L;
	private long available;

	protected AbstractEntityQueryResult(EntityQuery<A, R, T, E, M> request, long available) {		
		super(request);
		this.available = available;
	}

	@Override
	public long getAvailable() {
		return this.available;
	}
}
