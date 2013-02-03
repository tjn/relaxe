/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.CharType;

public final class CharKey<
	A extends Attribute,	
	E extends HasChar<A, E> & HasString<A, E>
>
	extends StringKey<A, E, CharType, CharHolder, CharKey<A, E>>
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
	
	private CharKey(HasCharKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}	
	
	public static <
		X extends Attribute,
		T extends HasChar<X, T> & HasString<X, T>
	>
	CharKey<X, T> get(HasCharKey<X, T> meta, X a) {
		CharKey<X, T> k = meta.getCharKey(a);
		
		if (k == null) {
			if (CharType.TYPE.equals(a.type())) {
				k = new CharKey<X, T>(meta, a);
			}
		}
				
		return k;
	}


	@Override
	public CharType type() {
		return CharType.TYPE;
	}
	
	public void set(E e, CharHolder newValue)
		throws EntityRuntimeException {
		e.setChar(this, newValue);
	}
	
	public CharHolder get(E e)
		throws EntityRuntimeException {
		return e.getChar(this);
	}

	@Override
	public CharHolder newHolder(String newValue) {
		return CharHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest)
		throws EntityRuntimeException {
		dest.setChar(this, src.getChar(this));
	}
	
	@Override
	public CharKey<A, E> self() {	
		return this;
	}
	
	@Override
	public CharHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return CharHolder.of(holder);
	}
}
