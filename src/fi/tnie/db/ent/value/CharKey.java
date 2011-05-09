/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.types.CharType;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public final class CharKey<
	A extends Attribute,
	T extends ReferenceType<T, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>
>
	extends AbstractPrimitiveKey<A, T, E, String, CharType, CharHolder, CharKey<A, T, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1117929153888182121L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private CharKey() {
	}
	
	private CharKey(EntityMetaData<A, ?, T, E, ?, ?, ?> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}	
	
	public static <
		X extends Attribute,	
		Z extends ReferenceType<Z, ?>,
		T extends Entity<X, ?, Z, T, ?, ?, ?>
	>
	CharKey<X, Z, T> get(EntityMetaData<X, ?, Z, T, ?, ?, ?> meta, X a) {
		CharKey<X, Z, T> k = meta.getCharKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.CHAR) {
				k = new CharKey<X, Z, T>(meta, a);
			}
		}
				
		return k;
	}	

	@Override
	public CharType type() {
		return CharType.TYPE;
	}
	
	public void set(E e, CharHolder newValue) {
		e.setChar(this, newValue);
	}
	
	public CharHolder get(E e) {
		return e.getChar(this);
	}

	@Override
	public CharHolder newHolder(String newValue) {
		return CharHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) {
		dest.setChar(this, src.getChar(this));
	}
	
	@Override
	public CharKey<A, T, E> self() {	
		return this;
	}
}
