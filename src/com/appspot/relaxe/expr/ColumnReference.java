/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.Collections;
import java.util.List;

import com.appspot.relaxe.meta.Column;


public class ColumnReference
	extends ColumnExpr 
	implements ValueExpression {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1430117431795245443L;
	
	private Column column;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	public ColumnReference() {
	}
		
	public ColumnReference(AbstractTableReference table, Column column) {
		super(table, column.getColumnName());
		
		this.column = column;
//		logger().debug("table-col-expr column-name: " + column.getColumnName().getName());
	}
	
	@Override
	public int getType() {				
		return getColumn().getDataType().getDataType();
	}

	@Override
	protected void traverseContent(VisitContext vc, ElementVisitor v) {
		AbstractTableReference tref = getTable();
						
		if (tref != null) {
			Identifier cn = tref.getCorrelationName(v.getContext());
			
			if (cn != null) {			
//				logger().debug("corr. name: " + cn);			
				cn.traverse(vc, v);
				Symbol.DOT.traverse(vc, v);
			}
		}
				
		getColumnName().traverse(vc, v);
	}
	
	
	public Column getColumn() {		
		return this.column;
	}
	
	@Override
	public List<? extends Identifier> getColumnNames() {		
		return Collections.singletonList(getColumnName());
	}

}
