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

public class ValueModelKey<
	A extends Attribute,	
	T extends ReferenceType<T, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P>,
	K extends PrimitiveKey<A, T, E, V, P, H, K>
>
{
	private K key;
	
	public ValueModelKey(K key) {
		super();
		this.key = key;
	}

	public K getKey() {
		return key;
	}
	
	public ValueModel<H> getAttributeModel(EntityModel<A, ?, T, E, ?, ?, ?, ?> m) throws EntityRuntimeException {		
		ValueModel<H> vm = m.getValueModel(getKey());
		return vm;
	}
}
