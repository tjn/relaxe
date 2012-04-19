/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.types.ReferenceType;


/**
 *
 *
 * @param <A>
 * @param <R>
 * @param <T>
 * @param <V>
 */

public abstract class ReferenceHolder<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, V, H, ?, M, C>,
	V extends Entity<A, R, T, V, H, ?, M, C>,
	H extends ReferenceHolder<A, R, T, V, H, M, C>,
	M extends EntityMetaData<A, R, T, V, H, ?, M, C>,
	C extends Content
>
	extends Holder<V, T> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7758303666346608268L;
	private V value;

	protected ReferenceHolder() {
		super();
	}

	public ReferenceHolder(V value) {
		super();
		this.value = value;
	}

	@Override
	public V value() {
		return this.value;
	}
	
		
//	public boolean identityEquals(H h) {
//		if (h == null) {
//			throw new NullPointerException("h");
//		}
//		
//		if (this.isNull()) {
//			return h.isNull();
//		}
//	}
}
