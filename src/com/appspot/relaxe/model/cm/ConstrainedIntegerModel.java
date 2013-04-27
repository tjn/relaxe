/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model.cm;


public class ConstrainedIntegerModel
	extends DefaultConstrainedMutableValueModel<Integer> {
	
	public ConstrainedIntegerModel() {
		super();
	}

	public ConstrainedIntegerModel(Integer value) {
		super(value);
	}
	
	public ConstrainedIntegerModel(int value) {
		this(Integer.valueOf(value));	
	}
}
