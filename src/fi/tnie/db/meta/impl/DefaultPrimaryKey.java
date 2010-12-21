/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Collections;
import java.util.List;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.PrimaryKey;

public class DefaultPrimaryKey 
	extends DefaultConstraint
	implements PrimaryKey {
	
	private DefaultMutableBaseTable table;
	private DefaultColumnMap columnMap = null; 

	@Override
	public BaseTable getTable() {
		return this.table;
	}
		
	public DefaultPrimaryKey(DefaultMutableBaseTable table, Identifier name) {
		super(table.getMutableSchema(), name);
		
		this.table = table;
		
//		if (columnList == null) {
//			throw new NullPointerException("columnList must not be null");
//		}
//	
//		if (columnList.isEmpty()) {
//			throw new IllegalArgumentException("columnList must not be empty");
//		}
//				
//		for (DefaultMutableColumn c : columnList) {
//			getColumnMap().add(c);
//		}
		
		table.setPrimaryKey(this);
	}
	
	public DefaultPrimaryKey(DefaultMutableBaseTable table, Identifier name, List<DefaultMutableColumn> columnList) {
		this(table, name);
		setColumnList(columnList);
	}
	
	public void setColumnList(List<DefaultMutableColumn> columnList) {		
		if (columnList == null) {
			throw new NullPointerException("columnList must not be null");
		}
		
		if (columnList.isEmpty()) {
			throw new IllegalArgumentException("columnList must not be empty");
		}
		
		for (DefaultMutableColumn c : columnList) {
			getColumnMap().add(c);
		}
	}
	
	@Override
	public List<? extends Column> columns() {
		return Collections.unmodifiableList(
				getColumnMap().getColumnList());		
	}

	@Override
	public Type getType() {
		return Type.PRIMARY_KEY;
	}

	private DefaultColumnMap getColumnMap() {
		if (columnMap == null) {			
			columnMap = new DefaultColumnMap(this.table);			
		}

		return columnMap;
	}

	@Override
	public Column getColumn(Identifier name) {
		return getColumnMap().get(name);
	}	
}
