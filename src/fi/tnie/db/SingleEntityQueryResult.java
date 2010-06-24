/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.List;


public class SingleEntityQueryResult<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>
>	extends AbstractEntityQueryResult<A, R, Q, E> 
{		
	private List<E> content;
	private E result;
	
	
	public SingleEntityQueryResult(EntityQuery<A, R, Q, E> source, E result, long available) {		
		super(source, available);
		
		if (result == null) {
			throw new NullPointerException("'result' must not be null");
		}
						
		this.result = result;
	}
	
	public List<E> getContent() {
		if (this.content == null) {
			this.content = Collections.singletonList(this.result);
		}
		
		return this.content;
	}

	public E first() {
		return this.result;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
