/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.expr;

public class LongLiteral
	extends SimpleElement
	implements Token {

	private long value;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	public LongLiteral() {
		this(0);
	}
	
	public LongLiteral(long value) {
		this.value = value;
	}

	@Override
	public String getTerminalSymbol() {
		return Long.toString(this.value);
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);		
		v.end(this);			
	}

	@Override
	public boolean isOrdinary() {
		return true;
	}		
}