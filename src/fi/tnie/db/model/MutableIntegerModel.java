/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class MutableIntegerModel
	extends DefaultMutableValueModel<Integer>
	implements IntegerModel {

	public MutableIntegerModel() {
		super();
	}

	public MutableIntegerModel(Integer value) {
		super(value);	
	}
	
	public MutableIntegerModel(int value) {
		super(Integer.valueOf(value));	
	}
		
	public void set(int value) {
		super.set(Integer.valueOf(value));
	}
}
