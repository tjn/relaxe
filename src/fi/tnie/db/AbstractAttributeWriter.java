/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractAttributeWriter<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P>,
	K extends PrimitiveKey<A, R, T, E, V, P, H, K>>
	implements AttributeWriter<A, R, T, E, V, P, H, K> {

	private K key;
	private int index;
	
	public AbstractAttributeWriter(K key, int index) {
		super();
		this.key = key;
		this.index = index;
	}

	public K getKey() {
		return key;
	}
	
	public int getIndex() {
		return index;
	}
	
	/**
	 * Returns <code>ph</code> as of type <code>H</code>.
	 *  
	 * @param ph
	 * @return
	 */
	protected abstract H as(PrimitiveHolder<?, ?> ph);
	
	@Override
	public H write(DataObject src, E dest) {
		PrimitiveHolder<?, ?> ph = src.get(getIndex());
		H h = as(ph);
		dest.set(getKey(), h);
		return h;
	}
}
