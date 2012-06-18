/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public abstract class StringKey<
	A extends Attribute,	
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>,	
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<String, P>,
	K extends PrimitiveKey<A, T, E, String, P, H, K>
	>
	extends AbstractPrimitiveKey<A, T, E, String, P, H, K>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected StringKey() {
	}	
	
	protected StringKey(EntityMetaData<A, ?, ?, E, ?, ?, ?, ?> meta, A name) {
		super(meta, name);		
	}
	
	public abstract K self();
}
