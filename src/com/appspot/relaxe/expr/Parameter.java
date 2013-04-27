/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.Collections;
import java.util.List;

import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataType;
import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.types.PrimitiveType;


/**
 * Value should be primitive holder
 * @author tnie
 *
 */
public abstract class Parameter<T extends PrimitiveType<T>, H extends PrimitiveHolder<?, T, H>>
	extends SimpleElement
	implements ValueExpression, SelectListElement, Token {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3615036325774581065L;
	private String name;
//	private int dataType;
	private DataType columnType;
	private ColumnName columnName;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected Parameter() {
	}
		
	public Parameter(Column column) {
		this.columnName = column.getColumnName();
		this.columnType = column.getDataType();
//		this.dataType = this.columnType.getDataType();
//		this.typeName = column.getDataType().getTypeName();		
//		this.type = column.getDataType().getDataType();
	}

	@Override
	public int getType() {
		return this.columnType.getDataType();
	}
	
	public abstract H getValue();

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
	
	public DataType getColumnType() {
		return this.columnType;
	}
	
	@Override
	public ColumnExpr getTableColumnExpr(int column) {
		if (column != 1) {
			throw new IndexOutOfBoundsException(Integer.toString(column));
		}
		
		return null;
	}
	
}
