/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;


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
		setQuery(query);
	}

	public QueryExpression getQuery() {
		return query;
	}

	public void setQuery(QueryExpression query) {
		if (query == null) {
			throw new NullPointerException("'query' must not be null");
		}

		this.query = query;
	}

	@Override
	public ElementList<? extends ColumnName> getUncorrelatedColumnNameList() {	
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
	public void addAll(ElementList<SelectListElement> dest) {
		getQuery().getTableExpr().getSelect().getSelectList().copyTo(dest);
	}

	@Override
	public int getColumnCount() {
		return getQuery().getTableExpr().getSelect().getColumnCount();
	}



	
	
}
