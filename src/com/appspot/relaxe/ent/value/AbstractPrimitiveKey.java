/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.value;

import java.io.Serializable;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


public abstract class AbstractPrimitiveKey<
	A extends Attribute,
	E,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P, H>,
	K extends PrimitiveKey<A, E, V, P, H, K>
>
	implements PrimitiveKey<A, E, V, P, H, K> {
	
	/**
	 *
	 */
	private static final long serialVersionUID = -3422141375386521175L;
	private A name;
	
	protected AbstractPrimitiveKey() {		
	}	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected AbstractPrimitiveKey(A name) {
		setName(name);
	}
		

	private void setName(A name) {
		if (name == null) {
			throw new NullPointerException("name");
		}
		
		this.name = name;
	}

	@Override
	public A name() {
		return this.name;
	}	

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			throw new NullPointerException("o");
		}
		
		if (o == this) {
			return true;
		}
		
		if(!getClass().equals(o.getClass())) {
			return false;
		}
		
		// Since getClass().equals(o.getClass()) implies t.type().getSqlType() == type().getSqlType()
		// we only need to check the name:		
		AbstractPrimitiveKey<?, ?, ?, ?, ?, ?> t = (AbstractPrimitiveKey<?, ?, ?, ?, ?, ?>) o;				
		return nameEquals(t);
	}
	
	private boolean nameEquals(AbstractPrimitiveKey<?, ?, ?, ?, ?, ?> pk) {		
		return name().equals(pk.name());
	}
	
	@Override
	public void copy(E src, E dest) {		
		set(dest, get(src));		
	}
	
	@Override
	public void reset(E dest) {
		set(dest, newHolder(null));		
	}
}
