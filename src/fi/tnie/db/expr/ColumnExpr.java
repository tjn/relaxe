/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public abstract class ColumnExpr 
	extends CompoundElement 
	implements ValueExpression {

	private AbstractTableReference table;	
	private ColumnName columnName;

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
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}

		return this;
	}	
}
