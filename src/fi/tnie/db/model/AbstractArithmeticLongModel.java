/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public abstract class AbstractArithmeticLongModel
	extends AbstractArithmeticModel<Long> {

	public AbstractArithmeticLongModel(ValueModel<Long> a, ValueModel<Long> b) {
		super(a, b);
	}
	
	
}
