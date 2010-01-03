/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public class SimpleColumnName 
	extends SimpleElement implements ColumnName {
	
	private String columnName;
	
	public SimpleColumnName(String columnName) {
		super();
		setColumnName(columnName);
	}

	@Override
	public String getTerminalSymbol() {
		return getColumnName();
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);	
		
	}

}
