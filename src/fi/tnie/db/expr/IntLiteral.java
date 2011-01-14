/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.expr;

public class IntLiteral
	extends SimpleElement {

	private int value;
	
	public IntLiteral(int value) {
		this.value = value;
	}

	@Override
	public String getTerminalSymbol() {
		return Integer.toString(this.value);
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);			
	}		
}