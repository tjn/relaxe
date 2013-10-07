/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.PrimitiveType;

public class StringLiteral	
	implements Token, ValueExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2349786152502099361L;
	private String quoted;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public StringLiteral() {
		this("");
	}
	
	public StringLiteral(String value) {
		this(value, false);
	}
	
	public StringLiteral(String value, boolean quoted) {
		if (value == null) {
			throw new NullPointerException("value");
		}
		
		if (quoted) {
			this.quoted = value;
		}
		else {
			StringBuilder buf = new StringBuilder(value.length() + 2 + 1);		
			buf.append('\'');
			buf.append(value.replaceAll("'", "''"));
			buf.append('\'');			
			this.quoted = buf.toString();
		}
	}

	@Override
	public String getTerminalSymbol() {
		return quoted;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {		
		v.start(vc, (Token) this);
		v.end(this);			
	}
	
	@Override
	public int getType() {
		return PrimitiveType.CHAR;
	}

	@Override
	public boolean isOrdinary() {
		return true;
	}
	
	@Override
	public Identifier getColumnName() {
		return null;
	}
}