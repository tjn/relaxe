/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public abstract class ColumnExpr 
	extends CompoundElement implements ValueExpression {

	private AbstractTableReference table;	

	public ColumnExpr(AbstractTableReference table) {
		super();
		this.table = table;		
	}

	public AbstractTableReference getTable() {
		return this.table;
	}
	
	public abstract String getName();
}
