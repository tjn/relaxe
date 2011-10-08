/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class LongAdditionModel
	extends AbstractArithmeticLongModel {

		
	public LongAdditionModel(ValueModel<Long> a, ValueModel<Long> b) {
		super(a, b);		
	}

	@Override
	protected Long compute(Long a, Long b) {
		if (a == null || b == null) {
			return null;
		}
		
		return Long.valueOf(a.longValue() + b.longValue());
	}

}
