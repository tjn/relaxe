/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.expr;

public enum JoinType	
	implements Element {
	INNER(SQLKeyword.INNER),
	LEFT(SQLKeyword.LEFT),
	RIGHT(SQLKeyword.RIGHT),
	FULL(SQLKeyword.FULL);
	
	private SQLKeyword keyword;
	
	private JoinType(SQLKeyword keyword) {
		this.keyword = keyword;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		vc = v.start(vc, this);
		
		this.keyword.traverse(vc, v);		
		SQLKeyword.JOIN.traverse(vc, v);
		
		v.end(this);
	}

	@Override
	public String getTerminalSymbol() {	
		return null;
	}
}