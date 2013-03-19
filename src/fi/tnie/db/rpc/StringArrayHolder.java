/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.types.ArrayType;
import fi.tnie.db.types.PrimitiveType;

public abstract class StringArrayHolder<
	E extends PrimitiveType<E>, 
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
