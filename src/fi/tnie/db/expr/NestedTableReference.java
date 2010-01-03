/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class NestedTableReference
	extends AbstractTableReference {

	private SelectQuery query;
			
	public NestedTableReference(SelectQuery query) {
		super();
		setQuery(query);
	}

	public SelectQuery getQuery() {
		return query;
	}

	public void setQuery(SelectQuery query) {
		if (query == null) {
			throw new NullPointerException("'query' must not be null");
		}

		this.query = query;
	}

	@Override
	public ElementList<? extends ColumnName> getColumnNameList() {
		
//		getQuery().getSelect().getSelectList().		
//		return query.getColumnNameList();
		// TODO:
		return null;
	}

	@Override
	public ElementList<SelectListElement> getSelectList() {
//		TODO: wild-card:
//		getCorrelationName().*
		
		// this is incorrect:
//		should use getCorrelationName().* or 
//		qualify column names of the query with new correlation symbol
		
		return getQuery().getSelect().getSelectList();
	}
	
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		
		Identifier cn = getCorrelationName(v.getContext());
		
		Symbol.PAREN_LEFT.traverse(vc, v);
		this.getQuery().traverse(vc, v);
		Symbol.PAREN_RIGHT.traverse(vc, v);
		Keyword.AS.traverse(vc, v);
		cn.traverse(vc, v);
		
		v.end(this);
	}
	
}
