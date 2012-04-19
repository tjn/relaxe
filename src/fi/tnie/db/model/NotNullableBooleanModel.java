/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class NotNullableBooleanModel
	extends MutableBooleanModel
	implements NotNullableModel<Boolean> {

	public NotNullableBooleanModel(boolean value) {
		super(Boolean.valueOf(value));
	}
	
	public NotNullableBooleanModel() {
		this(false);
	}

	public NotNullableBooleanModel(Boolean value) {
		super(value);
		
		if (value == null) {
			throw new NullPointerException("value");
		}
	}
	
	@Override
	public void set(Boolean newValue)
		throws NullPointerException {
		if (newValue == null) {
			throw new NullPointerException();
		}
	
		super.set(newValue);
	}
	
	@Override
	public final boolean isNullable() {
		return false;
	}
	
	public boolean booleanValue() {
		return get().booleanValue();
	}
}
