/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.im;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class ReferenceIdentityMap<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, ?, ?, ?>,
	E extends Entity<A, R, T, E, H, ?, ?, ?>,
	H extends ReferenceHolder<A, R, T, E, H, ?, ?>
>
	implements EntityIdentityMap<A, R, T, E, H> {

	@Override
	public H get(E key) throws EntityRuntimeException {
		return (key == null) ? null : key.ref();
	}

}
