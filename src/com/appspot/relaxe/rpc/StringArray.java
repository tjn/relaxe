/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.util.List;


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
		super(copy(content));
	}
	
	public StringArray(List<String> content) {
		super(copy(content.toArray(EMPTY)));
	}
	
	@Override
	public String[] toArray() {		
		return copy(this.getContent());
	}	
	
	public static String[] copy(String[] src) {
		String[] dest = new String[src.length];
		System.arraycopy(src, 0, dest, 0, src.length);
		return dest;
	}		
}
