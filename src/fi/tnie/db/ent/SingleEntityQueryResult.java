/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Collections;
import java.util.List;

import fi.tnie.db.types.ReferenceType;


public class SingleEntityQueryResult<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>	extends AbstractEntityQueryResult<A, R, T, E>
{
	private List<E> content;
	private E result;

	public SingleEntityQueryResult(EntityQuery<A, R, T, E> source, E result, long available) {
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
