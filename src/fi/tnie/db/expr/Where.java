/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.expr.op.AndPredicate;

public class Where extends AbstractClause {
	
	private Predicate searchCondition;
	
	public Where() {
		super(Keyword.WHERE);
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
	
//	@Override
//	public void traverse(VisitContext vc, ElementVisitor v) {
//		
//		
//		traverseNonEmpty(getSearchCondition(), vc, v);
//	}
}
