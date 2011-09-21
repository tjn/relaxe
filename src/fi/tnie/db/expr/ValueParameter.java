/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import java.util.Collections;
import java.util.List;

import fi.tnie.db.meta.Column;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;

public class ValueParameter<T extends PrimitiveType<T>, H extends PrimitiveHolder<?, T>>
	extends Parameter<T, H> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8609263476546435725L;
	private H value;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ValueParameter() {
	}
	
	public ValueParameter(Column column, H value) {
		super(column);
		this.value = value;
	}


	@Override
	public H getValue() {
		return value;
	}

	public void setValue(H value) {
		this.value = value;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		v.start(vc, this);
		v.end(this);
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public List<? extends ColumnName> getColumnNames() {
		return Collections.singletonList(null);
	}
	
	@Override
	public ValueExpression getColumnExpr(int column) {	
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
		
		return this;
	}	
}
