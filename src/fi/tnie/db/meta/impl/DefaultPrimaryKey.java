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
	private ColumnMap columnMap = null; 

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
	
	
//	public void setColumnList(List<DefaultMutableColumn> columnMap) {
//		if (columnMap == null) {
//			throw new NullPointerException("columnMap must not be null");
//		}
//		
//		if (columnMap.isEmpty()) {
//			throw new IllegalArgumentException("columnMap must not be empty");
//		}
//						
//		if (this.table != null) {
//			this.table.setPrimaryKey(null);
//			this.table = null;
//		}
//						
//		for (DefaultMutableColumn c : columnMap) {
//			if (this.table == null) {
//				this.table = (DefaultMutableBaseTable) c.getParentNode();				
//			}
//			else {				
//				ensureSameTable(c.getParentNode(), this.table, 
//						"all the columns of the multi-column primary key must originate from the same table");
//			}						
//		}
//		
//		this.table.setPrimaryKey(this);
//		this.columnList = new ArrayList<DefaultMutableColumn>(columnMap);
//	}
	
	@Override
	public List<? extends Column> columns() {
		return Collections.unmodifiableList(
				getColumnMap().getColumnList());		
	}

	@Override
	public Type getType() {
		return Type.PRIMARY_KEY;
	}

	private ColumnMap getColumnMap() {
		if (columnMap == null) {			
			columnMap = new ColumnMap(this.table);			
		}

		return columnMap;
	}

	@Override
	public Column getColumn(Identifier name) {
		return getColumnMap().get(name);
	}	
}
