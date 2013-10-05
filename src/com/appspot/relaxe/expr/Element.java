/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.io.Serializable;



/**
 * Syntactical element of SQL.
 * 
 *  
 *  
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */
public interface Element
	extends Serializable {
	
	public void traverse(VisitContext vc, ElementVisitor v);	
	String getTerminalSymbol();
	
	
}
