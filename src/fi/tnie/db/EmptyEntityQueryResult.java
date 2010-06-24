/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.List;


public class EmptyEntityQueryResult<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>
> extends AbstractEntityQueryResult<A, R, Q, E> {
		
	private List<E> content;
	
	public EmptyEntityQueryResult(EntityQuery<A, R, Q, E> source, long available) {		
		super(source, available);		
		this.content = Collections.emptyList();
	}
	
	public List<E> getContent() {
		return this.content;		
	}

	public E first() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}
}
