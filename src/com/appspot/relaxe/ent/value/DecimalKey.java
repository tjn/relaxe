/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.Decimal;
import com.appspot.relaxe.rpc.DecimalHolder;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.DecimalType;

public final class DecimalKey<
	A extends Attribute,
	E extends HasDecimal<A, E>
>
	extends AbstractPrimitiveKey<A, E, Decimal, DecimalType, DecimalHolder, DecimalKey<A, E>>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1332513199153161810L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private DecimalKey() {
	}

	private DecimalKey(HasDecimalKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,		
		T extends HasDecimal<X, T>
	>
	DecimalKey<X, T> get(HasDecimalKey<X, T> meta, X a) {
		DecimalKey<X, T> k = meta.getDecimalKey(a);
		
		if (k == null) {
			if (DecimalType.TYPE.equals(a.type())) {
				k = new DecimalKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}

	@Override
	public DecimalType type() {
		return DecimalType.TYPE;
	}

	public void set(E e, DecimalHolder newValue)
		throws EntityRuntimeException {
		e.setDecimal(this, newValue);
	}
	
	public DecimalHolder get(E e) 
		throws EntityRuntimeException {
		return e.getDecimal(this);
	}
	
	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setDecimal(this, src.getDecimal(this));
	}

	@Override
	public DecimalHolder newHolder(Decimal newValue) {
		return DecimalHolder.valueOf(newValue);
	}
	
	@Override
	public DecimalKey<A, E> self() {
		return this;
	}
	
	@Override
	public DecimalHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return DecimalHolder.of(holder);
	}
}
