/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Column;

public class ValueParameter extends Parameter {
	private Object value;
	
	public ValueParameter(Column column, Object value) {		
		this(column.getName(), column.getDataType().getDataType(), value);
	}

	public ValueParameter(String name, int type, Object value) {
		super(name, type);
		this.value = value;
	}

	@Override
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);
	}
}
