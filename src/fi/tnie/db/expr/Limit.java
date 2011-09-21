/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class Limit	
	extends SimpleClause {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -442124021423302569L;

	/**
	 * LIMIT ALL -clause
	 */
	public Limit() {
		super(Keyword.LIMIT, Keyword.ALL);		
	}
	/**
	 * LIMIT value -clause
	 * @param value
	 */	
	public Limit(int value) {
		super(Keyword.LIMIT, new IntLiteral(value));		
	}
	
	/**
	 * LIMIT value -clause
	 * @param value
	 */	
	public Limit(long value) {
		super(Keyword.LIMIT, new LongLiteral(value));		
	}
}
