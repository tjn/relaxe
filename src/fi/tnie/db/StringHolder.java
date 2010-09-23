/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class StringHolder
	extends Holder {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7895663698067736815L;

	private String value;
	
	public static final StringHolder NULL_HOLDER = new StringHolder();
	
	public StringHolder(String value) {
		this.value = value;
	}
	
	private StringHolder() {		
	}
	
	public String value() {
		return this.value;
	}
}
