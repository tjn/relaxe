/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public class Having extends AbstractClause {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6829774771176381103L;
	
	private Predicate searchCondition;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private Having() {
	}
	
	public Having(Predicate searchCondition) {
		super();
		
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
		SQLKeyword.HAVING.traverse(vc, v);		
	}
}
