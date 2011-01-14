/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class MutableBooleanModel
	extends DefaultMutableValueModel<Boolean>
	implements BooleanModel {

	public MutableBooleanModel() {
		super();
	}

	public MutableBooleanModel(Boolean value) {
		super(value);	
	}
	
	public MutableBooleanModel(boolean value) {
		super(Boolean.valueOf(value));	
	}
		
	public void set(boolean value) {	
		super.set(Boolean.valueOf(value));
	}
}
