/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Collections;
import java.util.List;

import fi.tnie.db.types.ReferenceType;

public class MultipleEntityQueryResult<
	A,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
> extends AbstractEntityQueryResult<A, R, T, E>{		

	private List<E> content;
			
	public MultipleEntityQueryResult(EntityQuery<A, R, T, E> source, List<E> content, long available) {		
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
