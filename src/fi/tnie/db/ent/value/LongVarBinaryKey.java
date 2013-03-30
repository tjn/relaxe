/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.LongVarBinaryHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.LongVarBinaryType;
import fi.tnie.db.types.PrimitiveType;

public final class LongVarBinaryKey<
	A extends Attribute,
	E extends HasLongVarBinary<A, E>
>
	extends AbstractPrimitiveKey<A, E, LongVarBinary, LongVarBinaryType, LongVarBinaryHolder, LongVarBinaryKey<A, E>>	
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9081341365015015217L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private LongVarBinaryKey() {
	}

	private LongVarBinaryKey(HasLongVarBinaryKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasLongVarBinary<X, T>
	>
	LongVarBinaryKey<X, T> get(HasLongVarBinaryKey<X, T> meta, X a) {
		LongVarBinaryKey<X, T> k = meta.getLongVarBinaryKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (t != null && LongVarBinaryType.TYPE.equals(t)) {
				k = new LongVarBinaryKey<X, T>(meta, a);
			}
		}
				
		return k;
	}
		
	@Override
	public LongVarBinaryType type() {
		return LongVarBinaryType.TYPE;
	}
	
	public void set(E e, LongVarBinaryHolder newValue) {
		e.setLongVarBinary(this, newValue);
	}
	
	public LongVarBinaryHolder get(E e) {
		return e.getLongVarBinary(self());
	}
	
	@Override
	public LongVarBinaryHolder newHolder(LongVarBinary newValue) {
		return LongVarBinaryHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) {
		dest.setLongVarBinary(this, src.getLongVarBinary(this));
	}

	@Override
	public LongVarBinaryKey<A, E> self() {
		return this;
	}

	@Override
	public void reset(E dest) throws EntityRuntimeException {
		dest.setLongVarBinary(this, LongVarBinaryHolder.NULL_HOLDER);
	}
		
	@Override
	public LongVarBinaryHolder as(PrimitiveHolder<?, ?, ?> unknown) {
		return unknown.asLongVarBinaryHolder();
	}
}
