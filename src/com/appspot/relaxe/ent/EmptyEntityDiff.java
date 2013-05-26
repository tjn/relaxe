/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.util.Collections;
import java.util.Map;

import com.appspot.relaxe.types.ReferenceType;


public class EmptyEntityDiff<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, ?, ?, M, ?>,
	E extends Entity<A, R, T, E, ?, ?, M, ?>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M, ?>
>
	extends AbstractEntityDiff<A, R, T, E, M>
{
	protected EmptyEntityDiff(E original) {
		super(original, original);
	}

	@Override
	public Map<A, Change> attributes() {
		return Collections.emptyMap();
	}

	@Override
	public Change change() {
		return null;
	}

	@Override
	public Map<R, Change> references() {
		return Collections.emptyMap();
	}
}