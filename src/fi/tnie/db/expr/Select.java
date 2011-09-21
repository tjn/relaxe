/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.ArrayList;
import java.util.List;


public class Select
	extends AbstractClause {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8112104453171815591L;
	
	private ElementList<SelectListElement> selectList;	
	private boolean distinct;	
	
	public Select() {
		super(Keyword.SELECT);
	}

	public ElementList<SelectListElement> getSelectList() {
		if (selectList == null) {
			selectList = new ElementList<SelectListElement>();			
		}

		return selectList;
	}
	
	public int getColumnCount() {
		int cc = 0;
		
		for(SelectListElement e : getSelectList().getContent()) {
			cc += e.getColumnCount();
		}
		
		return cc;
	}
	
	public List<ValueExpression> expandValueExprList() {
		ArrayList<ValueExpression> el = new ArrayList<ValueExpression>();
		
		for(SelectListElement e : getSelectList().getContent()) {
			int cc = e.getColumnCount();
			
			for (int i = 1; i <= cc; i++) {
				el.add(e.getColumnExpr(i));
			}
		}		
		
		return el;
	}
	
	/**
	 * NOTE: selected list contains contains <code>null</code> -element
	 * for those columns that are not column references.  
	 * 
	 * @return
	 */	
	public List<ColumnExpr> expandColumnExprList() {
		ArrayList<ColumnExpr> el = new ArrayList<ColumnExpr>();
		
		for(SelectListElement e : getSelectList().getContent()) {
			int cc = e.getColumnCount();
			
			for (int i = 1; i <= cc; i++) {								
				el.add(e.getTableColumnExpr(i));
			}
		}		
		
		return el;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return distinct;
	}
	
	public SelectListElement add(SelectListElement e) {
		if (e == null) {
			throw new NullPointerException("'e' must not be null");
		}
		
		getSelectList().add(e);
		return e;
	}
	
	public ColumnReference add(ColumnReference expr) {		
		// avoid renaming by passing null:
//		add(expr, (ColumnName) null);
		
		if (expr == null) {
			throw new NullPointerException("'expr' must not be null");
		}
				
		getSelectList().add(expr);
		
		return expr;
	}
	
	public ValueElement add(ValueExpression expr) {
		return add(expr, (ColumnName) null);
	}
	
	public ValueElement add(ValueExpression expr, ColumnName newName) {
		if (expr == null) {
			throw new NullPointerException("'expr' must not be null");
		}
		
		ValueElement e = new ValueElement(expr, newName);
		getSelectList().add(e);
		return e;
	}	
	
	public ValueElement add(ValueExpression expr, String newName) 
		throws IllegalIdentifierException {
		ColumnName n = null;
		
		if (newName != null) {
			n = ColumnName.create(newName);
		}
		
		return add(expr, n);		
	}	
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getClause().traverse(vc, v);
		
		if (this.distinct) {
			Keyword.DISTINCT.traverse(vc, v);
		}
		
		getContent().traverse(vc, v);	
	}
	
	@Override
	protected Element getContent() {		
		return getSelectList();
	}
		
	public ElementList<? extends ColumnName> getColumnNameList() {		
		ElementList<ColumnName> cl = null;
		ElementList<SelectListElement> p = getSelectList();
		
		if (!p.isEmpty()) {
			cl = new ElementList<ColumnName>();
			
			for (SelectListElement e : p.getContent()) {
				cl.getContent().addAll(e.getColumnNames());				
			}
		}
		
		return cl;
	}	
}
