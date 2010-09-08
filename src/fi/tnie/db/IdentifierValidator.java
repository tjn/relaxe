/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.expr.IllegalIdentifierException;

public interface IdentifierValidator {
	
	/**
	 * Returns true, if and only the name is a valid 
	 * identifier (ordinary or delimited).
	 * 
	 * @param name
	 * @return
	 */
	
	public boolean isValid(String name);
	
	/**
	 * Returns true, if and only the name is a valid ordinary identifier.
	 * 
	 * @param name
	 * @param ordinary
	 * @return
	 */
	public boolean isValid(String name, boolean ordinary);
	
	void validate(String token, boolean ordinary)
		throws IllegalIdentifierException;
}
