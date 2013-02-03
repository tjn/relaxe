/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;


public class StringArray
	extends DefaultArray<String> {
	
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
}
