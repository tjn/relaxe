/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public interface PrimitiveAccessor<
	A extends Attribute,
	E,
	S extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<S, P, H>,	
	K extends PrimitiveKey<A, E, S, P, H, K>
>
	extends Serializable
{
	K key();	
	S get();
	
	
	H getHolder();
}
