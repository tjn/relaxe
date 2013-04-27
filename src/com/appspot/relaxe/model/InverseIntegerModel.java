/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model;

public class InverseIntegerModel
	extends DefaultImmutableModel<Integer>
	implements ImmutableValueModel<Integer> {
		
	public InverseIntegerModel(ValueModel<Integer> inner) {
		super(inner);
	}
	
	@Override
	public Integer get() {
		Integer v = super.get();
		return (v == null) ? null : Integer.valueOf(-v.intValue());		
	}
}
