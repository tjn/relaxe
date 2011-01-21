/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public interface Key<
	A extends Attribute,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P>,
	E extends Entity<A, ?, ?, E>,
	K extends Key<A, V, P, H, E, K>
>
	extends Serializable
{
	P type();
	A name();
	Value<A, V, P, H, E, K> value(E e);

	Value<A, V, P, H, E, K> newValue();
}
