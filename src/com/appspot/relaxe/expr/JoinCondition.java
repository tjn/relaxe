/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public abstract class JoinCondition 
	extends CompoundElement
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6371602240694158705L;
	private AbstractTableReference left;
	private AbstractTableReference right;
		
	
//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		
//	}

	public AbstractTableReference getLeft() {
		return left;
	}

	public AbstractTableReference getRight() {
		return right;
	}
		
}
