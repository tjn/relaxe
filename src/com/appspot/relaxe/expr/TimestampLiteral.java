/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.types.PrimitiveType;

public class TimestampLiteral	
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
	private TimestampLiteral() {
	}
	
	public TimestampLiteral(int yyyy, int mm, int dd, int hh, int min, int ss, int sss) {
		this(yyyy, mm, dd, hh, min, ss, sss, DateTimeLiteral.DEFAULT_TIME_PRECISION);
	}
	
	public TimestampLiteral(int yyyy, int mm, int dd, int hh, int min, int ss, int sss, int precision) {
		StringBuilder buf = new StringBuilder(40);		
		buf.append(SQLKeyword.TIMESTAMP.toString());
		buf.append(" ");
		buf.append('\'');
		appendDate(buf, yyyy, mm, dd);
		buf.append(' ');
		appendTime(buf, hh, min, ss, sss, precision);
		buf.append('\'');
		
		this.symbol = buf.toString();		
	}
	
	@Override
	public String getTerminalSymbol() {
		return this.symbol;
	}
	
	
	@Override
	public int getType() {
		return PrimitiveType.TIMESTAMP;
	}
}