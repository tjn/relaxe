/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.HasKey;
import fi.tnie.db.rpc.LongHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.LongType;
import fi.tnie.db.types.PrimitiveType;

public final class LongKey<
	A extends Attribute,
	E extends HasLong<A, E>
>
	extends AbstractPrimitiveKey<A, E, Long, LongType, LongHolder, LongKey<A, E>>	
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private LongKey() {
	}

	private LongKey(HasLongKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasLong<X, T> & HasKey<LongType, LongKey<X, T>, T>
	>
	LongKey<X, T> get(HasLongKey<X, T> meta, X a) {
		LongKey<X, T> k = meta.getLongKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (t != null && t.getSqlType() == PrimitiveType.INTEGER) {
				k = new LongKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}
		
	@Override
	public LongType type() {
		return LongType.TYPE;
	}
	
	public void set(E e, LongHolder newValue) 
		throws EntityRuntimeException {
		e.setLong(this, newValue);
	}
	
	public LongHolder get(E e) 
		throws EntityRuntimeException {
		return e.getLong(self());
	}
	
	@Override
	public LongHolder newHolder(Long newValue) {
		return LongHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setLong(this, src.getLong(this));
	}

	@Override
	public LongKey<A, E> self() {
		return this;
	}

	@Override
	public void reset(E dest) throws EntityRuntimeException {
		dest.setLong(this, LongHolder.NULL_HOLDER);
	}
	
	@Override
	public LongHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return LongHolder.of(holder);
	}
}