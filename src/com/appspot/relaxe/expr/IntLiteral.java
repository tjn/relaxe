/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.PrimitiveType;

public class IntLiteral	
	implements Token, ValueExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2349786152502099361L;
	private int value;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public IntLiteral() {
		this(0);
	}
	
	public IntLiteral(int value) {
		this.value = value;
	}

	@Override
	public String getTerminalSymbol() {
		return Integer.toString(this.value);
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {		
		v.start(vc, (Token) this);
		v.end(this);			
	}
	
	@Override
	public int getType() {
		return PrimitiveType.INTEGER;
	}

	@Override
	public boolean isOrdinary() {
		return true;
	}
	
	@Override
	public ColumnName getColumnName() {
		return null;
	}
}