/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public abstract class SimpleClause
	extends AbstractClause {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3193883178079657128L;
	private Element content;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SimpleClause() {
	}
	
	private SQLKeyword clause;

	public SimpleClause(SQLKeyword clause, Element clauseContent) {
		if (clause == null) {
			throw new NullPointerException("clause");
		}
		
		this.clause = clause;
		
		if (clauseContent == null) {
			throw new NullPointerException("content");
		}
		
		this.content = clauseContent;
	}	

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		traverseClause(vc, v);
		getContent().traverse(vc, v);						
		v.end(this);		
	}
	
	@Override
	protected void traverseClause(VisitContext vc, ElementVisitor v) {
		this.clause.traverse(vc, v);
	}

	@Override
	protected Element getContent() {
		return this.content;
	}
}
