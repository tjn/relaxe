/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;


public interface Element {
	
	public void traverse(VisitContext vc, ElementVisitor v);	
	String getTerminalSymbol();	
}
