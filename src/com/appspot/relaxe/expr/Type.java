/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;



/**
 * Syntactical element of SQL.
 * 
 *  
 *  
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */
public interface Type	
	extends Element {
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v);	
	@Override
	String getTerminalSymbol();
}
