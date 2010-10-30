/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityQueryResult <
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, ? extends E>>
	implements EntityQueryResult<A, R, Q, T, E>
{
	private EntityQuery<A, R, Q, T, E> source; 
	private long available;
	
	protected AbstractEntityQueryResult(EntityQuery<A, R, Q, T, E> source, long available) {		
		if (source == null) {
			throw new NullPointerException("'source' must not be null");
		}
		
		this.source = source;
		this.available = available;
	}
	
	@Override
	public EntityQuery<A, R, Q, T, E> getSource() {		
		return this.source;
	}
	
	@Override
	public long getAvailable() {		
		return this.available;
	}

}
