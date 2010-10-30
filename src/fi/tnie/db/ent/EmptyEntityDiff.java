/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Collections;
import java.util.Map;

import fi.tnie.db.types.ReferenceType;

public class EmptyEntityDiff<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,	
	Q extends Enum<Q> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, Q, T, ? extends E>
>
	extends AbstractEntityDiff<A, R, Q, T, E>
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
	public Map<R, fi.tnie.db.ent.Change> references() {	
		return Collections.emptyMap();
	}
}
