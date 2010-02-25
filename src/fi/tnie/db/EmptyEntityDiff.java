/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.Map;

public class EmptyEntityDiff<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,	
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>
>
	extends AbstractEntityDiff<A, R, Q, E>
{
	protected EmptyEntityDiff(E original) {
		super(original, original);
	}

	@Override
	public Map<A, EntityDiff.Change> attributes() {
		return Collections.emptyMap();
	}

	@Override
	public EntityDiff.Change change() {
		return null;
	}

	@Override
	public Map<R, fi.tnie.db.EntityDiff.Change> references() {	
		return Collections.emptyMap();
	}
}
