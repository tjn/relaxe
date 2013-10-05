/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.List;


public class NestedTableReference
	extends NonJoinedTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8816662022129528101L;
	private QueryExpression query;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected NestedTableReference() {
	}
			
	public NestedTableReference(QueryExpression query) {
		super();
		
		if (query == null) {
			throw new NullPointerException("query");
		}
		
		this.query = query;
	}

	public QueryExpression getQuery() {
		return query;
	}

	@Override
	public ElementList<? extends Identifier> getUncorrelatedColumnNameList() {	
		return getQuery().getTableExpr().getSelect().getColumnNameList();
	}
		
	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.getQuery().traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
		getCorrelationClause().traverse(vc, v);
	}

	@Override
	public void addAll(List<SelectListElement> dest) {
		ElementList<SelectListElement> el = getQuery().getTableExpr().getSelect().getSelectList();		
		dest.addAll(el.getContent());
	}

	@Override
	public int getColumnCount() {
		return getQuery().getTableExpr().getSelect().getColumnCount();
	}



	
	
}
