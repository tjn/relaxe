/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;


public class StringArray
	extends AbstractArray<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2522909517688093544L;
	private static final String[] EMPTY = {};

	public StringArray() {
		super(EMPTY);
	}
	
	public StringArray(StringArray src) {
		super(src.getContent());
	}
	
	public StringArray(ArrayValue<String> src) {
		super(src);
	}

	public StringArray(String[] content) {
		super(content.clone());
	}
	
	@Override
	public String[] toArray() {		
		return this.getContent().clone();
	}
	
}
