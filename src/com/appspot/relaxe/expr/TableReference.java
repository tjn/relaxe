/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;
import java.util.List;

import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Table;


public class TableReference 
	extends NonJoinedTable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3388366557843780574L;
	private Table table;	
	private ElementList<Identifier> columnNameList;
	
	private SchemaElementName tableName;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected TableReference() {
	}
		
	public TableReference(Table table) {
		super();
		
		if (table == null) {
			throw new NullPointerException("'table' must not be null");
		}
		
		this.table = table;
	}
	
	@Override
	public void traverseContent(VisitContext vc, ElementVisitor v) {
		getTableName().traverse(vc, v);		
		getCorrelationClause().traverse(vc, v);
	}
	
	public Table getTable() {
		return table;
	}

	@Override
	public ElementList<? extends Identifier> getUncorrelatedColumnNameList() {
		if (columnNameList == null) {
			this.columnNameList = new ElementList<Identifier>();
			
			List<Identifier> nl = this.columnNameList.getContent();
			
			for (final Column c : getTable().columnMap().values()) {
				nl.add(c.getColumnName());
			}
		}
		
		return columnNameList;
	}
		
	public SchemaElementName getTableName()
		throws IllegalIdentifierException {
		if (tableName == null) {
//			tableName = new SchemaElementName(getTable());
			SchemaElementName sen = getTable().getName();
			tableName = sen;
		}

		return tableName;
	}

	@Override
	public void addAll(List<SelectListElement> dest) {
		for (Column	c : getTable().columnMap().values()) {
			dest.add(new ColumnReference(this, c));
		}
	}
	
	@Override
	public int getColumnCount() {		
		return getTable().columnMap().size();
	}
}
