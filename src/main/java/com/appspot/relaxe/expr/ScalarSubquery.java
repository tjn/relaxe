package com.appspot.relaxe.expr;

public class ScalarSubquery
	implements ValueExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2242762357678292139L;
		
	private QueryExpression query;
	
	public ScalarSubquery(QueryExpression query) {		
		super();
		
		if (query == null) {
			throw new NullPointerException("query");
		}
		
		Select sc = query.getTableExpr().getSelect();
		
		if (sc.getColumnCount() != 1) {
			throw new IllegalArgumentException("Invalid column count: " + sc.getColumnCount());
		}
		
		this.query = query;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);		
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.query.traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);		
		v.end(this);
	}



	@Override
	public String getTerminalSymbol() {
		return null;
	}

	@Override
	public ValueExpression asValueExpression() {
		return this;
	}

	@Override
	public Element asNullSpecification() {
		return null;
	}

	@Override
	public Element asDefaultSpecification() {
		return null;
	}

	@Override
	public int getType() {
		return 0;
	}

	@Override
	public Identifier getColumnName() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
