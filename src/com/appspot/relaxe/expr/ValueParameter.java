/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


public class ValueParameter<
	V extends Serializable,
	T extends PrimitiveType<T>, 
	H extends PrimitiveHolder<V, T, H>
>
	extends AbstractParameter<V, T, H> 
{
	
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
		Parameter<?, ?, ?> p = this;
		v.start(vc, p);
		v.end(p);
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public List<? extends Identifier> getColumnNames() {
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
