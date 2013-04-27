/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.VarcharHolder;
import com.appspot.relaxe.types.VarcharType;

public final class VarcharKey<
	A extends Attribute,
	E extends HasVarchar<A, E> & HasString<A, E>
>
	extends StringKey<A, E, VarcharType, VarcharHolder, VarcharKey<A, E>>
{
	/**
	 *
	 */
	private static final long serialVersionUID = 128524051109455630L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private VarcharKey() {
	}

	private VarcharKey(HasVarcharKey<A, E> meta, A name) {
		super(name);
		meta.register(this);
	}
	
	public static <
		X extends Attribute,
		T extends HasVarchar<X, T> & HasString<X, T>
	>
	VarcharKey<X, T> get(HasVarcharKey<X, T> meta, X a) {
		VarcharKey<X, T> k = meta.getVarcharKey(a);
		
		if (k == null) {						
			if (VarcharType.TYPE.equals(a.type())) {
				k = new VarcharKey<X, T>(meta, a);
			}			
		}
				
		return k;
	}
	
	@Override
	public VarcharType type() {
		return VarcharType.TYPE;
	}
	
	public void set(E e, VarcharHolder newValue) 
		throws EntityRuntimeException {
		e.setVarchar(this, newValue);
	}
	
	public VarcharHolder get(E e) 
		throws EntityRuntimeException {
		return e.getVarchar(this);
	}
	
	@Override
	public VarcharHolder newHolder(String newValue) {
		return VarcharHolder.valueOf(newValue);
	}

	@Override
	public void copy(E src, E dest) 
		throws EntityRuntimeException {
		dest.setVarchar(this, src.getVarchar(this));		
	}
	
	@Override
	public VarcharKey<A, E> self() {
		return this;
	}
	
	@Override
	public VarcharHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return VarcharHolder.of(holder);
	}
}
