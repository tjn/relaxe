/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public interface Mutator<
	A extends Attribute,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>,
	S extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<S, P>,	
	K extends PrimitiveKey<A, T, E, S, P, H, K>
>
	extends PrimitiveAccessor<A, T, E, S, P, H, K>
{
	void set(S newValue)
		throws EntityException;
	void setHolder(H newHolder)
		throws EntityException;
}
