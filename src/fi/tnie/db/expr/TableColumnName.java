/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Column;

public class TableColumnName
	extends ColumnName {
	
	private Column column;	
	
	public TableColumnName(Column column) {
		super(column.getName());
		setColumn(column);
	}

//	@Override
//	public String getColumnName() {
//		return getName();
//	}

	@Override
	public String getTerminalSymbol() {
		return getName();
	}

	public Column getColumn() {
		return column;
	}

	private void setColumn(Column column) {
		this.column = column;
	}

	@Override
	public boolean isOrdinary() {
		return true;
	}
}
