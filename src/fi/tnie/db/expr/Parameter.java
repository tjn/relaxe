/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.Collections;
import java.util.List;

import fi.tnie.db.meta.Column;

public abstract class Parameter
	extends SimpleElement
	implements ValueExpression, Token {
	
	private String name;
	private int type;	
	private ColumnName columnName;
		
	public Parameter(Column column) {
		this.columnName = column.getColumnName();
		this.type = column.getDataType().getDataType();
	}

	@Override
	public int getType() {
		return this.type;
	}
	
	public abstract Object getValue();

	public String getName() {
		return name;
	}

	@Override
	public boolean isOrdinary() {
			return false;
	}

	@Override
	public String getTerminalSymbol() {		
		return "?";
	}

	@Override
	public ColumnName getColumnName() {
		return this.columnName;
	}

	@Override
	public int getColumnCount() {
			return 1;
	}

	@Override
	public List<? extends ColumnName> getColumnNames() {
		return Collections.singletonList(this.columnName);
	}
	
	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);
	}

	@Override
	public ValueExpression getColumnExpr(int column) {
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
		
		return this;
	}
	
	
	
}
