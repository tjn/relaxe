/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class NullModel<S>
	extends BooleanTransformationModel<S> {

	public NullModel(ValueModel<S> source) {
		super(source);
	}
	
	@Override
	public Boolean transform(S source) {
		return Boolean.valueOf(source == null);
	}
}
