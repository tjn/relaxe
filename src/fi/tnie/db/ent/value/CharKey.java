/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.types.CharType;

public class CharKey<A extends Attribute, E extends Entity<A, ?, ?, E>>
	extends PrimitiveKey<A, String, CharType, CharHolder, E, CharKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1117929153888182121L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private CharKey() {
	}

	public CharKey(A name) {
		super(name);
	}

	@Override
	public CharType type() {
		return CharType.TYPE;
	}
	
	public void set(E e, CharHolder newValue) {
		e.setChar(this, newValue);
	}
	
	public CharHolder get(E e) {
		return e.get(this);
	}

	@Override
	public CharHolder newHolder(String newValue) {
		return CharHolder.valueOf(newValue);
	}
	

//	@Override
//	public CharValue<A, E> newValue() {
//		return new CharValue<A, E>(this);
//	}

//	public Value<A, String, CharType, CharHolder, E, CharKey<A,E>> value(E e) {
//		return e.value(this);
//	}
}
