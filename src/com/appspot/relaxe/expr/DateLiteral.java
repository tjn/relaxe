/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.PrimitiveType;

public class DateLiteral	
	extends DateTimeLiteral {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2349786152502099361L;
	private String symbol;	
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DateLiteral() {
	}
	
	public DateLiteral(int yyyy, int mm, int dd) {
		StringBuilder buf = new StringBuilder(17);
		
		buf.append(SQLKeyword.DATE.getTerminalSymbol());
		buf.append(' ');
		buf.append('\'');				
		appendDate(buf, yyyy, mm, dd);
		buf.append('\'');
		
		this.symbol = buf.toString();		
	}

	@Override
	public String getTerminalSymbol() {
		return this.symbol;
	}
	
	@Override
	public int getType() {
		return PrimitiveType.DATE;
	}
}