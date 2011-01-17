/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class ConstantBooleanModel
	extends ConstantValueModel<Boolean>
	implements BooleanModel
{
	public static final BooleanModel NULL = new ConstantBooleanModel(null);
	public static final BooleanModel TRUE = new ConstantBooleanModel(true);  
	public static final BooleanModel FALSE = new ConstantBooleanModel(false);
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private ConstantBooleanModel() {
	}
	
	public ConstantBooleanModel(boolean value) {
		this(Boolean.valueOf(value));		
	}
	
	public ConstantBooleanModel(Boolean value) {
		super(value);		
	}
}
