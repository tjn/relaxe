/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.ArrayList;
import java.util.List;

public abstract class AllColumns implements SelectListElement, Token {

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
	
	protected abstract TableRefList getTableRefs();
}
