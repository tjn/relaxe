/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public class Having extends AbstractClause {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6829774771176381103L;

	public Having() {
	}

	private Predicate searchCondition;
	
//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		Predicate p = getSearchCondition();
//		
//		if (p != null) {
//			dest.append("HAVING ");
//			p.generate(qc, dest);			
//		}
//	}

	public void setSearchCondition(Predicate predicate) {
		this.searchCondition = predicate;
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
