/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.im;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.AbstractPrimitiveKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public class AbstractKeyIdentityMap<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, RH, ?, M, ?>,
	E extends Entity<A, R, T, E, RH, ?, M, ?>,
	RH extends ReferenceHolder<A, R, T, E, RH, M, ?>,
	M extends EntityMetaData<A, R, T, E, RH, ?, M, ?>,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P, H>,	
	K extends AbstractPrimitiveKey<A, E, V, P, H, K>
>
	extends AbstractIdentityMap<A, R, T, E, RH, M, V>
{
	private K key;
	
	public AbstractKeyIdentityMap(K key) {
		super();
		
		if (key == null) {
			throw new NullPointerException("key");
		}
		
		this.key = key;
	}
	
	@Override
	protected V identify(E src) 
		throws EntityRuntimeException {
		H h = src.get(this.key);
		return (h == null) ? null : h.value();
	}
}
