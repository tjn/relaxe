/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public abstract class AbstractArithmeticLongModel<A extends Number, B extends Number>
	extends AbstractArithmeticModel<Long, A, B> {

	public AbstractArithmeticLongModel(ValueModel<A> a, ValueModel<B> b) {
		super(a, b);
	}
}
