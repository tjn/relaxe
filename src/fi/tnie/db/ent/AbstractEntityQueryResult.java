/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityQueryResult <
	A extends Serializable,
	R, 
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>>
	implements EntityQueryResult<A, R, T, E>
{
	private EntityQuery<A, R, T, E> source; 
	private long available;
	
	protected AbstractEntityQueryResult(EntityQuery<A, R, T, E> source, long available) {		
		if (source == null) {
			throw new NullPointerException("'source' must not be null");
		}
		
		this.source = source;
		this.available = available;
	}
	
	@Override
	public EntityQuery<A, R, T, E> getSource() {		
		return this.source;
	}
	
	@Override
	public long getAvailable() {		
		return this.available;
	}
}
