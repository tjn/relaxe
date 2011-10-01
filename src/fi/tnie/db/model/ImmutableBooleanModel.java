/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public abstract class ImmutableBooleanModel
	extends AbstractBooleanModel
	implements BooleanModel, ImmutableValueModel<Boolean> {

	@Override
	public final MutableBooleanModel asMutable() {
		return null;
	}

	@Override
	public ConstantValueModel<Boolean> asConstant() {
		return DefaultConstantValueModel.valueOf(get());
	}

	@Override
	public ImmutableValueModel<Boolean> asImmutable() {
		return this;
	}

	@Override
	public abstract Boolean get();

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public final boolean isMutable() {
		return false;
	}

}
