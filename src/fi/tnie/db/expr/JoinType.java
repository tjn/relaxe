/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.expr;

public enum JoinType	
	implements Element {
	INNER(Keyword.INNER),
	LEFT(Keyword.LEFT),
	RIGHT(Keyword.RIGHT),
	FULL(Keyword.FULL);
	
	private Keyword keyword;	

	private JoinType(Keyword keyword) {
		this.keyword = keyword;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		vc = v.start(vc, this);
		
		this.keyword.traverse(vc, v);		
		Keyword.JOIN.traverse(vc, v);
		
		v.end(this);
	}

	@Override
	public String getTerminalSymbol() {	
		return null;
	}
}