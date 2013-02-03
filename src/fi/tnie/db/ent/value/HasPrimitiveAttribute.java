/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.HasKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public interface HasPrimitiveAttribute<
	A extends Attribute,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P, H>,	
	K extends PrimitiveKey<A, E, V, P, H, K>,
	E extends HasPrimitiveAttribute<A, V, P, H, K, E>
	>
	extends HasKey<P, K, E>
{
	
}
