/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.PrimitiveType;

public abstract class StringHolder<T extends PrimitiveType<T>, H extends StringHolder<T, H>>
	extends PrimitiveHolder<String, T, H> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7895663698067736815L;
	private String value;
		
	public StringHolder(String value) {
		this.value = value;		
	}
	
	protected StringHolder() {		
	}
	
	@Override
	public String value() {
		return this.value;
	}
}
