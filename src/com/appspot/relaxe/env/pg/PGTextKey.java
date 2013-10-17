/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.value.HasString;
import com.appspot.relaxe.ent.value.StringKey;
import com.appspot.relaxe.rpc.PrimitiveHolder;


public class PGTextKey<
	A extends Attribute,
	E extends HasPGText<A, E> & HasString<A, E>	
>
	extends StringKey<A, E, PGTextType, PGTextHolder, PGTextKey<A, E>>
{	
	/**
	 *
	 */
	private static final long serialVersionUID = 3465654564903987460L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private PGTextKey() {
	}
	
	private PGTextKey(HasPGTextKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasPGText<X, T> & HasString<X, T>
	>
	PGTextKey<X, T> get(HasPGTextKey<X, T> meta, X a) {
		PGTextKey<X, T> k = meta.getPGTextKey(a);
		
		if (k == null) {						
			if (PGTextType.TYPE.equals(a.type())) {
				k = new PGTextKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}


			
	@Override
	public PGTextType type() {
		return PGTextType.TYPE;
	}
	
	@Override
	public void set(E e, PGTextHolder newValue) 
		throws EntityRuntimeException {
		e.setPGText(this, newValue);
	}
	
	@Override
	public PGTextHolder get(E e) 
		throws EntityRuntimeException {
		return e.getPGText(this);
	}
	
		
//	@Override
//	public PGTextHolder newHolder(PGText newValue) {
//		return PGTextHolder.valueOf(newValue);
//	}

	@Override
	public void copy(E src, E dest) {
		dest.setPGText(this, src.getPGText(this));
	}

	@Override
	public PGTextKey<A, E> self() {
		return this;
	}

	@Override
	public PGTextHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return PGTextHolder.of(holder);
	}

	@Override
	public PGTextHolder newHolder(String newValue) {
		return PGTextHolder.valueOf(newValue);
	}
}
