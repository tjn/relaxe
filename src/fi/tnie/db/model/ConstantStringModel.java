/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model;

public class ConstantStringModel
	extends DefaultConstantValueModel<String>
	implements StringModel
{
	public static final StringModel NULL = new ConstantStringModel(null);
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private ConstantStringModel() {
	}
	
	public ConstantStringModel(String value) {
		super(value);		
	}
}
