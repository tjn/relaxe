/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public class DefaultImmutableModel<V>
	implements ImmutableValueModel<V> {
	
	private ValueModel<V> inner;

	public DefaultImmutableModel(ValueModel<V> inner) {
		super();
		
		if (inner == null) {
			throw new NullPointerException("wrapped model");
		}
		
		this.inner = inner;
	}

	@Override
	public Registration addChangeHandler(ChangeListener<V> ch) {
		return this.inner.addChangeHandler(ch);
	}

	@Override
	public final MutableValueModel<V> asMutable() {
		return null;		
	}

	@Override
	public V get() {
		return this.inner.get();
	}
	
	@Override
	public ImmutableValueModel<V> asImmutable() {	
		return this;
	}
	
	@Override
	public boolean isMutable() {	
		return false;
	}
		
	@Override
	public boolean isConstant() {
		return this.inner.isConstant();
	}
	
	@Override
	public ConstantValueModel<V> asConstant() {
		ValueModel<V> im = this.inner;		
		return im.isConstant() ? im.asConstant() : new DefaultConstantValueModel<V>(im.get());
	}	
}
