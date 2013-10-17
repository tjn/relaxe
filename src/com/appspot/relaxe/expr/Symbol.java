/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public enum Symbol implements Token {
		
	EQUALS("="),	
	DOT("."),
	COMMA(","),	
	PAREN_LEFT("("),
	PAREN_RIGHT(")"),
	LESS_THAN("<"),
	GREATER_THAN(">"),
	LESS_OR_EQUAL("<="),
	GREATER_OR_EQUAL(">="),
	ASTERISK("*"),
	BRACKET_LEFT("["),
	BRACKET_RIGHT("]"),
	;
	
	private String token; 
	
	private Symbol(String t) {
		this.token = t;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);
	}
	
	@Override
	public String toString() {		
		return this.token;
	}

	@Override
	public String getTerminalSymbol() {		
		return this.token;
	}

	@Override
	public boolean isOrdinary() {
		return false;
	}
}
