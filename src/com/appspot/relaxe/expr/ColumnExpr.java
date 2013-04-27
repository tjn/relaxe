/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

public abstract class ColumnExpr 
	extends CompoundElement 
	implements ValueExpression, SelectListElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6471819124970538933L;
	private AbstractTableReference table;	
	private ColumnName columnName;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ColumnExpr() {
	}

	public ColumnExpr(AbstractTableReference table, ColumnName columnName) {
		super();
		this.table = table;		
		this.columnName = columnName;
	}

	public AbstractTableReference getTable() {
		return this.table;
	}
	
	public ColumnName getColumnName() {
		return columnName;
	}

	@Override
	public int getColumnCount() {	
		return 1;
	}

	@Override
	public ValueExpression getColumnExpr(int column) {
		return getTableColumnExpr(column);
	}

	@Override
	public ColumnExpr getTableColumnExpr(int column) {
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}

		return this;
	}
	
}
