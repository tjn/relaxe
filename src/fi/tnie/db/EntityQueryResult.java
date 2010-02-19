/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.List;


public class EntityQueryResult<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	E extends Entity<A, R>
> {		

	private List<E> content;
	private EntityQuery<A, R, E> source;
	private long available;
	
	public EntityQueryResult(EntityQuery<A, R, E> source, List<E> content, long available) {		
		super();
		
		if (source == null) {
			throw new NullPointerException("'source' must not be null");
		}
		
		this.source = source;
		this.content = content;
		this.available = available;
	}

	public List<E> getContent() {
		if (content == null) {
			return Collections.emptyList();
		}
		
		return Collections.unmodifiableList(this.content);
	}

	public EntityQuery<A, R, E> getSource() {
		return source;
	}

	public long getAvailable() {
		return available;
	}

	public E first() {
		return (available > 0) ? this.content.get(0) : null;
	}
}
