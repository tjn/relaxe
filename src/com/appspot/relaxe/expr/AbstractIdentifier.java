/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.meta.Folding;


public abstract class AbstractIdentifier
	extends SimpleElement
	implements Identifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2304652443656237941L;
	private String name;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractIdentifier() {
	}
	
	public AbstractIdentifier(String name)
		throws IllegalIdentifierException {
		
		if (!isValid(name)) {
			// validate to throw an exception with a detailed message: 
			validate(name);
		}
		
		this.name = name;
	}
	
	public static boolean isValid(String name) {
		return isValid(name, null);
	}	
	
	public String getName() {
		return this.name;
	}
	
	public abstract String getTerminalSymbol();	
	
	public static boolean isValid(String name, StringBuilder details) {
		if (name == null) {
			return fail("'name' must not be null", details);			
		}
		
		int nl = name.length(); 
		
		if (nl == 0) {
			return fail("'name' must not be empty", details);
		}
		
		int max = getMaxLength();
		
		if (nl > max) {
			return fail("length of the name exceeds the maximum allowed: " + max, details);
		}		
		
		return true;
	}
	
	public static void validate(String token)
		throws IllegalIdentifierException {

		StringBuilder details = new StringBuilder();
		
		if (!isValid(token, details)) {
			throw new IllegalIdentifierException(details.toString());
		}
	}
	
	public static int getMaxLength() {
		return 128;
	}
	
	static boolean fail(String msg, StringBuilder details) {
		if (details != null) {
			details.append(msg);
		}
		
		return false;
	}
	
	public static Identifier create(String name) {
		return create(name, Folding.UPPERCASE);
	}
	
	
	public static Identifier create(String name, Folding folding)
		throws IllegalIdentifierException {
	
		if (name == null) {
			throw new NullPointerException("'name' must not be null");
		}
		
		if (!isValid(name)) {
			validate(name); // throws an exception with a detailed message 
		}
					
		return OrdinaryIdentifier.isValidOrdinary(name) ?
				new OrdinaryIdentifier((folding == null) ? name : folding.apply(name)) : 
				new DelimitedIdentifier(name);
	}
	
}