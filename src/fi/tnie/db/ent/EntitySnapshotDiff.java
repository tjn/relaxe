/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Collections;
import java.util.Map;

import fi.tnie.db.types.ReferenceType;

public class EntitySnapshotDiff <
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
>
	extends AbstractEntityDiff<A, R, T, E, M>
{
	private Map<A, Change> attributes = null;
	private Map<R, Change> references = null;

	public EntitySnapshotDiff(E original, E modified) throws EntityRuntimeException {
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
