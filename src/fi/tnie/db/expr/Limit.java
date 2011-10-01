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
		super(SQLKeyword.LIMIT, SQLKeyword.ALL);		
	}
	/**
	 * LIMIT value -clause
	 * @param value
	 */	
	public Limit(int value) {
		super(SQLKeyword.LIMIT, new IntLiteral(value));		
	}
	
	/**
	 * LIMIT value -clause
	 * @param value
	 */	
	public Limit(long value) {
		super(SQLKeyword.LIMIT, new LongLiteral(value));		
	}
}
