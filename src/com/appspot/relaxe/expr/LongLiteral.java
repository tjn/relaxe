/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.PrimitiveType;

public class LongLiteral
	extends SimpleElement
	implements ValueExpression, Token {

	/**
	 * 
	 */
	private static final long serialVersionUID = 190166533024410267L;
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
		v.start(vc, (Token) this);
		v.end(this);	
	}

	@Override
	public boolean isOrdinary() {
		return true;
	}

	@Override
	public int getType() {
		return PrimitiveType.BIGINT;
	}

	@Override
	public Identifier getColumnName() {
		return null;
	}		
}