/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public abstract class JoinCondition 
	extends CompoundElement 
	implements Predicate {
	
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
