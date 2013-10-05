/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public class Where extends AbstractClause {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3968959510502629791L;
	private Predicate searchCondition;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private Where() {
	}
	
	public Where(Predicate searchCondition) {
		if (searchCondition == null) {
			throw new NullPointerException("searchCondition");
		}		
		
		this.searchCondition = searchCondition;
	}

	public Predicate getSearchCondition() {		
		return searchCondition;
	}

	@Override
	protected Element getContent() {		
		return getSearchCondition();
	}
	
	@Override
	protected void traverseClause(VisitContext vc, ElementVisitor v) {
		SQLKeyword.WHERE.traverse(vc, v);		
	}
}
