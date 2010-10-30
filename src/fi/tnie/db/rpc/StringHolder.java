/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import fi.tnie.db.types.Type;

public abstract class StringHolder<T extends Type<T>>
	extends Holder<String, T> {
	
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
