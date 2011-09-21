/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.io.Serializable;


public interface Element
	extends Serializable {
	
	public void traverse(VisitContext vc, ElementVisitor v);	
	String getTerminalSymbol();	
}
