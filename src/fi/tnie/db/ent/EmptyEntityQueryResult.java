/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Collections;
import java.util.List;

import fi.tnie.db.types.ReferenceType;


public class EmptyEntityQueryResult<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
> extends AbstractEntityQueryResult<A, R, T, E, M> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6678574047389644766L;
	private List<E> content;

	public EmptyEntityQueryResult(EntityQuery<A, R, T, E, M> request, long available) {
		super(request, available);
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
