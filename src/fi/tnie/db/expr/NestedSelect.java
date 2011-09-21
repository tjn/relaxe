/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class NestedSelect
	extends QueryExpression	
	implements TableExpression {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1754416875449914000L;
	private QueryExpression inner;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected NestedSelect() {
	}

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
	public Limit getLimit() {	
		return null;
	}
	
	@Override
	public Offset getOffset() {	
		return null;
	}
	
	@Override
	public Where getWhere() {
		return null;
	}

	@Override
	public TableExpression getTableExpr() {
		return this.inner.getTableExpr();
	}
}
