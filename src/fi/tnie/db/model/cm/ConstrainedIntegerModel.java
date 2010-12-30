/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.cm;


public class ConstrainedIntegerModel
	extends ConstrainedMutableValueModel<Integer> {
	
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
