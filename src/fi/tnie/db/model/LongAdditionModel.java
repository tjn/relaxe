/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class LongAdditionModel<A extends Number, B extends Number>
	extends AbstractArithmeticLongModel<A, B> {
		
	public LongAdditionModel(ValueModel<A> a, ValueModel<B> b) {
		super(a, b);		
	}

	@Override
	protected Long compute(A a, B b) {
		if (a == null || b == null) {
			return null;
		}
		
		return Long.valueOf(a.longValue() + b.longValue());
	}
}
