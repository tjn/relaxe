/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class Offset 
	extends SimpleClause {
		
	public Offset(int value) {
		super(Keyword.OFFSET, new IntLiteral(value));		
	}
}