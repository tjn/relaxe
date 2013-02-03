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

public interface Mutator<
	A extends Attribute,
	E extends Entity<A, ?, ?, E, ?, ?, ?, ?>,
	S extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<S, P, H>,	
	K extends PrimitiveKey<A, E, S, P, H, K>
>
	extends PrimitiveAccessor<A, E, S, P, H, K>
{
	void set(S newValue)
		throws EntityException;
	void setHolder(H newHolder)
		throws EntityException;
}
