/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public class DefaultConstantValueModel<V>
	extends AbstractValueModel<V>
	implements ConstantValueModel<V> {
	
	private V value;
	
// 	public static final ConstantValueModel<Long> LONG_1 = DefaultConstantValueModel.valueOf(0);

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected DefaultConstantValueModel() {
	}
	
	public DefaultConstantValueModel(V value) {
		super();
		this.value = value;
	}

	private static final Registration registration = new Registration() {		
		@Override
		public void remove() {			
		}
	};
	
	@Override
	public final Registration addChangeHandler(ChangeListener<V> ch) {
		return registration;
	}

	@Override
	public final V get() {
		return this.value;
	}

	@Override
	public final ConstantValueModel<V> asConstant() {
		return this;
	}

	@Override
	public final boolean isConstant() {
		return true;
	}
	
	
	public static <V>
	ConstantValueModel<V> valueOf(V newValue) {
		return new DefaultConstantValueModel<V>(newValue);
	}
	
	public static ConstantValueModel<Long> valueOf(long v) {
		return DefaultConstantValueModel.valueOf(Long.valueOf(v));
	}
	
	public static ConstantValueModel<Integer> valueOf(int v) {
		return DefaultConstantValueModel.valueOf(Integer.valueOf(v));
	}
}
