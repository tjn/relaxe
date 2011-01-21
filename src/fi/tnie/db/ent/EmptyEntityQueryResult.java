/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Collections;
import java.util.List;

import fi.tnie.db.types.ReferenceType;


public class EmptyEntityQueryResult<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
> extends AbstractEntityQueryResult<A, R, T, E> {

	private List<E> content;

	public EmptyEntityQueryResult(EntityQuery<A, R, T, E> source, long available) {
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
