/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.List;

/**
 * @author Administrator
 *
 */
public class TableColumns
	extends CompoundElement
	implements SelectListElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8004995546047752690L;
	private NonJoinedTable tableRef;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected TableColumns() {
	}

	public TableColumns(NonJoinedTable tableRef) {
		super();
		
		if (tableRef == null) {
			throw new NullPointerException("'tableRef' must not be null");
		}
		
		this.tableRef = tableRef;
	}

	@Override
	public List<? extends Identifier> getColumnNames() {	
		return tableRef.getColumnNameList().getContent();
	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {		
		v.start(vc, this);
		
		OrdinaryIdentifier cn = tableRef.getCorrelationName(v.getContext());				
		cn.traverse(vc, v);
		Symbol.DOT.traverse(vc, v);
		Symbol.ASTERISK.traverse(vc, v);
		
		v.end(this);
	}

	@Override
	public int getColumnCount() {
		return tableRef.getColumnNameList().getContent().size();
	}
	
	@Override
	public ValueExpression getColumnExpr(int column) {
		return getTableColumnExpr(column);
	}

	@Override
	public ColumnExpr getTableColumnExpr(int column) {
		int cc = getColumnCount();
		int index = column - 1;
		
		if (index < 0 || index >= cc) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
			 
		return tableRef.getAllColumns().getTableColumnExpr(column);
	}		
}
