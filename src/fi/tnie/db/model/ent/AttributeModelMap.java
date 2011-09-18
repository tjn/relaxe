/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.ent;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public interface AttributeModelMap<
	A extends Attribute,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P>,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>,
	D extends AttributeModelMap<A, V, P, H, T, E, D>
>	
{	
	D self();
	
	<			
		K extends PrimitiveKey<A, T, E, V, P, H, K>
	>	
	ValueModel<H> attr(K k) throws EntityRuntimeException;		
}
