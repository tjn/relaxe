/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public abstract class AbstractEntityQueryResult <
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>>
	implements EntityQueryResult<A, R, Q, E>
{
	private DefaultEntityQuery<A, R, Q, E> source; 
	private long available;
	
	protected AbstractEntityQueryResult(DefaultEntityQuery<A, R, Q, E> source, long available) {		
		if (source == null) {
			throw new NullPointerException("'source' must not be null");
		}
		
		this.source = source;
		this.available = available;
	}
	
	@Override
	public DefaultEntityQuery<A, R, Q, E> getSource() {		
		return this.source;
	}
	
	@Override
	public long getAvailable() {		
		return this.available;
	}

}
