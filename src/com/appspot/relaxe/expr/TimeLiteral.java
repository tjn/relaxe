/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.PrimitiveType;

public class TimeLiteral	
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
	private TimeLiteral() {
	}
	
	
	public TimeLiteral(int hh, int min, int ss, int sss, int precision) {
		StringBuilder buf = new StringBuilder(22);
		
		buf.append(SQLKeyword.TIME.getTerminalSymbol());
		buf.append(' ');
		buf.append('\'');
		appendTime(buf, hh, min, ss, sss, precision);
		buf.append('\'');
		
		this.symbol = buf.toString();		
	}	
	
	public TimeLiteral(int hh, int min, int ss) {
		StringBuilder buf = new StringBuilder(15);
		
		buf.append(SQLKeyword.TIME.getTerminalSymbol());
		buf.append(' ');
		buf.append('\'');
		appendTime(buf, hh, min, ss);
		buf.append('\'');
		
		this.symbol = buf.toString();		
	}
	
	@Override
	public String getTerminalSymbol() {
		return this.symbol;
	}
	
	
	@Override
	public int getType() {
		return PrimitiveType.TIME;
	}
}