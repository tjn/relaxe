/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Collections;
import java.util.Map;

public class EntitySnapshotDiff <
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,	
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>
>
	extends AbstractEntityDiff<A, R, Q, E>
{
	private Map<A, Change> attributes = null; 
	private Map<R, Change> references = null;
	
	public EntitySnapshotDiff(E original, E modified) {
		super(original, modified);
		this.attributes = Collections.unmodifiableMap(super.attributes(original, modified));
		this.references = Collections.unmodifiableMap(super.references(original, modified));
	}

	@Override
	public Map<A, Change> attributes() {
		return this.attributes;
	}

	@Override
	public Map<R, Change> references() {	
		return this.references;
	}
	
	
}
