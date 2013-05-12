/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.AbstractPrimitiveType;

public abstract class StringArrayHolder<
	E extends AbstractPrimitiveType<E>, 
	T extends ArrayType<T, E>, 
	H extends StringArrayHolder<E, T, H>
>
	extends ArrayHolder<String, StringArray, E, T, H> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 4156368459784864451L;
	
	private String[] content = {};

	public StringArrayHolder() {		
	}

	public StringArrayHolder(String[] data) {
		this.content = data.clone();
	}
	
	public StringArrayHolder(ArrayValue<String> value) {
		this.content = value.toArray();
	}
	
	public StringArrayHolder(H value) {
		StringArrayHolder<E, T, H> h = value;
		this.content = h.content;
	}
	
	public String[] getContent() {
		return content.clone();
	}
}
