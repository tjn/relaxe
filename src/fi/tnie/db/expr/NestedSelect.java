/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class NestedSelect
	extends QueryExpression	
	implements TableExpression {
	
	private QueryExpression inner;

	public NestedSelect(QueryExpression inner) {
		super();
		
		if (inner == null) {
			throw new NullPointerException("'inner' must not be null");
		}
		
		this.inner = inner;
	}	
		
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);		
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.inner.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
		v.end(this);		
	}

	@Override
	public Select getSelect() {
		return this.getTableExpr().getSelect();
	}

	@Override
	public OrderBy getOrderBy() {
		return null;
	}

	@Override
	public TableExpression getTableExpr() {
		return this.inner.getTableExpr();
	}
}
