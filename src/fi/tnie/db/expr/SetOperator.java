/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.expr.op.BinaryOperator;

public class SetOperator
	extends BinaryOperator
	implements TableExpression {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1520583086311992257L;
	private Op operator;
	private SQLKeyword all;
	private TableExpression left;
	private TableExpression right;
	
	public enum Op {
		UNION(SQLKeyword.UNION),
		INTERSECT(SQLKeyword.INTERSECT),
		EXCEPT(SQLKeyword.EXCEPT),
		;
		
		private SQLKeyword name;
		
		private Op(SQLKeyword name) {
			this.name = name;
		}
	}
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SetOperator() {
	}

	public SetOperator(Op operator, boolean all, TableExpression left, TableExpression right) {
		super(operator.name, left, right);
		
		if (operator == null) {
			throw new NullPointerException("'operator' must not be null");
		}
		
		if (left == null) {
			throw new NullPointerException("'left' must not be null");
		}
		
		if (right == null) {
			throw new NullPointerException("'right' must not be null");
		}
		
		this.operator = operator;
		this.left = left;
		this.right = right;
		this.all = all ? SQLKeyword.ALL : null;
	}

	public boolean isAll() {
		return this.all != null;
	}

	public void setAll(boolean all) {
		this.all = all ? SQLKeyword.ALL : null;
	}	
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		
		v.start(vc, this);
				
		left.traverse(vc, v);
		operator.name.traverse(vc, v);
		
		if (this.all != null) {
			this.all.traverse(vc, v);						
		}
		
		right.traverse(vc, v);
		
		v.end(this);
	}
		
	@Override
	public Select getSelect() {		
		return this.left.getSelect();
	}
	
	@Override
	public Where getWhere() {
		return null;
	}

	@Override
	public From getFrom() {
		return null;
	}

	@Override
	public GroupBy getGroupBy() {
		return null;
	}
	
//	@Override
//	public QueryExpression asQueryExpression() {
////		return new  
//	}
}
