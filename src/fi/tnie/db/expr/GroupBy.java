/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class GroupBy extends AbstractClause {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3190593291892514712L;

	public GroupBy() {
		super(SQLKeyword.GROUP_BY);
	}

	private ElementList<ValueExpression> groupingExprList;	
	
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
}
