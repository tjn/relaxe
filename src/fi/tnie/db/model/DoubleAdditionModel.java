/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class DoubleAdditionModel<A extends Number, B extends Number>
	extends AbstractArithmeticModel<Double, A, B> {
		
	public DoubleAdditionModel(ValueModel<A> a, ValueModel<B> b) {
		super(a, b);
	}

	@Override
	protected Double compute(A a, B b) {
		if (a == null || b == null) {
			return null;
		}
		
		return Double.valueOf(a.doubleValue() + b.doubleValue() + getConstant());
	}

	public double getConstant() {
		return 0;
	}
	
	
	
}
