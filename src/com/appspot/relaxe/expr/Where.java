/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.expr.op.AndPredicate;

public class Where extends AbstractClause {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3968959510502629791L;
	private Predicate searchCondition;
	
	public Where() {		
	}
	
	public Where(Predicate searchCondition) {
		this();
		this.searchCondition = searchCondition;
	}
	
		
	
//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {	
//		Predicate p = getSearchCondition();
//		
//		if (p != null) {
//			dest.append("WHERE ");
//			p.generate(qc, dest);			
//		}		
//	}	

	public Predicate getSearchCondition() {		
		return searchCondition;
	}

	public void setSearchCondition(Predicate searchCondition) {
		this.searchCondition = searchCondition;
	}
	
	/**
	 * AND's new search predicate with the current.
	 * @param searchCondition
	 */
		
	public void add(Predicate searchCondition) {
		Predicate p = getSearchCondition();
		setSearchCondition(AndPredicate.newAnd(p, searchCondition));		
	}
	
	@Override
	protected Element getContent() {		
		return getSearchCondition();
	}
	
	@Override
	protected void traverseClause(VisitContext vc, ElementVisitor v) {
		SQLKeyword.WHERE.traverse(vc, v);		
	}
	
//	@Override
//	public void traverse(VisitContext vc, ElementVisitor v) {
//		
//		
//		traverseNonEmpty(getSearchCondition(), vc, v);
//	}
}
