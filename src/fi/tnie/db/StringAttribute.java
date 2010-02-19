/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public class StringAttribute<A extends Enum<A>>
	extends AbstractAttribute<A, String> {
		
	private String value;
	
	public StringAttribute(A name, String value) {
		super(name);
	}

	@Override
	public String getValue() {	
		return this.value;
	}	
}
