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

public final class CharKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, String, CharType, CharHolder, E, CharKey<A, E>>
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
	
	private CharKey(EntityMetaData<A, ?, ?, E> meta, A name) {
		super(meta, name);
		meta.addKey(this);
	}	
	
	public static <
		X extends Attribute,
		T extends Entity<X, ?, ?, T>
	>
	CharKey<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
		CharKey<X, T> k = meta.getCharKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = meta.getAttributeType(a);
			
			if (t != null && t.getSqlType() == PrimitiveType.CHAR) {
				k = new CharKey<X, T>(meta, a);
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
	
//	@Override
//	public CharKey<A, E> normalize(EntityMetaData<A, ?, ?, E> meta) {
//		return meta.getCharKey(name());
//	}		
}
