/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.SchemaElementName;

public abstract class AbstractImmutableBaseTable
	extends ImmutableSchemaElement
	implements BaseTable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2458408040261055930L;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractImmutableBaseTable() {
	}
		
	protected AbstractImmutableBaseTable(Environment environment, SchemaElementName tableName) {
		super(environment, tableName);
	}

	@Override
	public String getTableType() {
		return BASE_TABLE;
	}

	@Override
	public boolean isBaseTable() {
		return true;
	}

	@Override
	public abstract SchemaElementMap<ForeignKey> foreignKeys();

	@Override
	public abstract PrimaryKey getPrimaryKey();
	
	@Override
	public boolean isPrimaryKeyColumn(Column column) {
		PrimaryKey pk = getPrimaryKey();
		
		if (pk == null) {
			return false;
		}
		
		return pk.getColumnMap().contains(column.getUnqualifiedName());
	}

	@Override
	public BaseTable asBaseTable() {
		return this;
	}
}
