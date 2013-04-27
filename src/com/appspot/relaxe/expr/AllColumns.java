/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.ArrayList;
import java.util.List;

public abstract class AllColumns 
	implements SelectListElement, Token {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7004307634380311562L;
	private Symbol allColumns = Symbol.ASTERISK;	
	
	public AllColumns() {
		super();
	}

	@Override
	public List<? extends ColumnName> getColumnNames() {
		TableRefList refs = getTableRefs(); 
		
		if (refs == null) {
			return null;
		}	
		
		int rc = refs.getCount();
		
		if (rc == 1) {
			return refs.getItem(0).getColumnNameList().getContent();
		}
		
		List<ColumnName> nl = new ArrayList<ColumnName>();
		
		for (int i = 0; i < rc; i++) {
			nl.addAll(refs.getItem(i).getColumnNameList().getContent());
		}
		
		return nl;
	}
	


	@Override
	public String getTerminalSymbol() {
		return allColumns.getTerminalSymbol();
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		allColumns.traverse(vc, v);
	}

	@Override
	public boolean isOrdinary() {
		return this.allColumns.isOrdinary();
	}

	@Override
	public int getColumnCount() {
		int cc = 0;
		
		TableRefList refs = getTableRefs();
				
		int rc = refs.getCount();
		
		for (int i = 0; i < rc; i++) {
			AbstractTableReference tref = refs.getItem(i);
			cc += tref.getColumnCount();			
		}
		
		return cc;
	}
	
	public ValueExpression getColumnExpr(final int column) {
		return getTableColumnExpr(column);
	}
	
	
	@Override
	public ColumnExpr getTableColumnExpr(final int column) {
		int c = 0;	
		ColumnExpr e = null;
		
		int index = column - 1;
		
		if (index < 0 || index >= getColumnCount()) {
			throw new IndexOutOfBoundsException("column=" + column);
		}
		
		TableRefList refs = getTableRefs();				
		int rc = refs.getCount();
		
		for (int i = 0; i < rc; i++) {
			AbstractTableReference tref = refs.getItem(i);
			c += tref.getColumnCount();
			
			if (column <= c) {
				int pos = c - column;				
				e = tref.getAllColumns().getTableColumnExpr(pos);
				break;
			}
		}
		
		return e;	
	}

	protected abstract TableRefList getTableRefs();
}
