/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Collections;
import java.util.Map;

import fi.tnie.db.types.ReferenceType;

public class EntitySnapshotDiff <
	A, 
	R,
	T extends ReferenceType<T>, 
	E extends Entity<A, R, T, ? extends E>
>
	extends AbstractEntityDiff<A, R, T, E>
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
