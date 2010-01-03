/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;


/**
 * 
 * 
 */
public interface Token
	extends Element
{	
	/**
	 * Returns true, if this token is ordinary token.
	 * If this token is delimiter token, returns false.
	 * @return
	 */	
	boolean isOrdinary();
	
	
	@Override
	public String getTerminalSymbol();
}
