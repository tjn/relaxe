/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.Collections;
import java.util.List;

import fi.tnie.db.meta.Column;

public class ValueParameter extends Parameter {
	private Object value;
	
	public ValueParameter(Column column, Object value) {
		super(column);
//		this(column.getName(), column.getDataType().getDataType(), value);
		this.value = value;
	}

//	public ValueParameter(String name, int type, Object value) {
//		super(name, type);
//		this.value = value;
//	}

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

	@Override
	public ColumnName getColumnName() {
		return null;
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public List<? extends ColumnName> getColumnNames() {
		return Collections.singletonList(null);
	}
}
