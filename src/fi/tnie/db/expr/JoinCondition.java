package fi.tnie.db.expr;

import fi.tnie.db.QueryContext;

public abstract class JoinCondition implements Predicate {
	
	private AbstractTableReference left;
	private AbstractTableReference right;
	
	
	
	@Override
	public void generate(QueryContext qc, StringBuffer dest) {
		
	}

	public AbstractTableReference getLeft() {
		return left;
	}

	public AbstractTableReference getRight() {
		return right;
	}

}
