/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public class NotNullableIntegerModel
	extends MutableIntegerModel
	implements NotNullableModel<Integer> {

	public NotNullableIntegerModel(int value) {
		super(Integer.valueOf(value));
	}
	
	public NotNullableIntegerModel() {
		this(0);
	}

	public NotNullableIntegerModel(Integer value) {
		super(value);
		
		if (value == null) {
			throw new NullPointerException("value");
		}
	}
	
	@Override
	public void set(Integer newValue)
		throws NullPointerException {
		if (newValue == null) {
			throw new NullPointerException();
		}
	
		super.set(newValue);
	}
	
	@Override
	public final boolean isNullable() {
		return false;
	}
	
	public int intValue() {
		return get().intValue();
	}
}
