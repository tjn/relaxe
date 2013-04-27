/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;


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
	
	/**
	 * Terminal symbol for the token must always be non-null. 
	 */
	
	@Override
	public String getTerminalSymbol();
}
