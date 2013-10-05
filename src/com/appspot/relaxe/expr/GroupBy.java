/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.Collection;

public class GroupBy extends AbstractClause {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3190593291892514712L;

	private ElementList<ValueExpression> groupingExprList;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private GroupBy() {
	}
	
	public GroupBy(ElementList<ValueExpression> expressionList) {
		super();
		
		if (expressionList == null) {
			throw new NullPointerException("expressionList");
		}
		
		this.groupingExprList = expressionList;
	}
	
	public GroupBy(Collection<ValueExpression> expressionList) {
		this(new ElementList<ValueExpression>(expressionList));
	}
		
	
//	@Override
//	public void generate(SimpleQueryContext qc, StringBuffer dest) {
//		ElementList<ValueExpression> el = getGroupingExprList();
//		
//		if (!el.isEmpty()) {
//			dest.append("GROUP BY ");
//			el.generate(qc, dest);
//		}		
//	}

	public ElementList<ValueExpression> getGroupingExprList() {
		if (groupingExprList == null) {
			groupingExprList = new ElementList<ValueExpression>();			
		}

		return groupingExprList;
	}
	
	@Override
	protected Element getContent() {		
		ElementList<ValueExpression> gl = getGroupingExprList();
		return gl.isEmpty() ? null : gl;
	}
	
	@Override
	protected void traverseClause(VisitContext vc, ElementVisitor v) {
		SQLKeyword.GROUP.traverse(vc, v);
		SQLKeyword.BY.traverse(vc, v);	
	}
}
