/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.DoubleType;
import fi.tnie.db.types.PrimitiveType;

public final class DoubleKey<	
	A extends Attribute,	
	E extends HasDouble<A, E>
>
	extends AbstractPrimitiveKey<A, E, Double, DoubleType, DoubleHolder, DoubleKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1065150474303051699L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DoubleKey() {
	}

	private DoubleKey(HasDoubleKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasDouble<X, T>
	>
	DoubleKey<X, T> get(HasDoubleKey<X, T> meta, X a) {
		DoubleKey<X, T> k = meta.getDoubleKey(a);
		
		if (k == null) {
			PrimitiveType<?> t = a.type();
			
			if (DoubleType.TYPE.equals(t)) {
				k = new DoubleKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public DoubleType type() {
		return DoubleType.TYPE;
	}

	public void set(E e, DoubleHolder newValue) 
		throws EntityRuntimeException {
		e.setDouble(this, newValue);
	}
	
	public DoubleHolder get(E e) 
		throws EntityRuntimeException {
		return e.getDouble(this);
	}

	@Override
	public DoubleHolder newHolder(Double newValue) {
		return DoubleHolder.valueOf(newValue);
	}
	
	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setDouble(this, src.getDouble(this));
	}
	
	@Override
	public DoubleKey<A, E> self() {
		return this;
	}
	
	@Override
	public DoubleHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return DoubleHolder.of(holder);
	}
}
