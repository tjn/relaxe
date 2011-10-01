/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class ConstantIntegerModel
	extends DefaultConstantValueModel<Integer>
	implements IntegerModel
{
	public static final IntegerModel NULL = new ConstantIntegerModel(null);	  
	public static final IntegerModel ZERO = new ConstantIntegerModel(0);
	public static final IntegerModel ONE = new ConstantIntegerModel(1);
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private ConstantIntegerModel() {
	}
	
	public ConstantIntegerModel(int value) {
		this(Integer.valueOf(value));		
	}
	
	public ConstantIntegerModel(Integer value) {
		super(value);		
	}
}
