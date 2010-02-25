/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.List;


public class MultipleEntityQueryResult<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>
> extends AbstractEntityQueryResult<A, R, Q, E>{		

	private List<E> content;
			
	public MultipleEntityQueryResult(EntityQuery<A, R, Q, E> source, List<E> content, long available) {		
		super(source, available);
		
		if (content == null) {
			throw new NullPointerException("'content' must not be null");
		}
		
		if (content.isEmpty()) {
			throw new IllegalArgumentException("'content' must not be empty here");
		}
			
		this.content = Collections.unmodifiableList(content);		
	}

	public List<E> getContent() {
		return this.content;
	}

	public E first() {
		return this.content.get(0);
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
