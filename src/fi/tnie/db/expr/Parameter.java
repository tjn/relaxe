/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Column;

public abstract class Parameter
	extends SimpleElement
	implements ValueExpression, Token {
	
	private String name;
	private int type;
		
	public Parameter(Column column) {
		this(column.getName(), column.getDataType().getDataType());
	}

	public Parameter(String name, int type) {
		super();
		this.name = name;
		this.type = type;	
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
}
