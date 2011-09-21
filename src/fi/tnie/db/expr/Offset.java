/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class Offset 
	extends SimpleClause {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -6671391761056533768L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	public Offset() {
		this(0);
	}
	
	public Offset(int value) {
		super(Keyword.OFFSET, new IntLiteral(value));		
	}
	
	public Offset(long value) {
		super(Keyword.OFFSET, new LongLiteral(value));		
	}
}