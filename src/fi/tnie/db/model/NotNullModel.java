/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class NotNullModel<S>
	extends AbstractTransformationModel<Boolean, S>
	implements BooleanModel {

	public NotNullModel(ValueModel<S> source) {
		super(source);
	}
	
	@Override
	public Boolean transform(S source) {
		return Boolean.valueOf(source != null);
	}
	
	@Override
	public MutableBooleanModel asMutable() {
		return null;
	}
}
