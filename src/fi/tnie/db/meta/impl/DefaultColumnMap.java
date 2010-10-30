/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.meta.ColumnMap;
import fi.tnie.db.meta.Table;

public class DefaultColumnMap
	extends DefaultElementMap<DefaultMutableColumn>
	implements ColumnMap, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6715516216855716542L;
	private Table table;
	private List<DefaultMutableColumn> columnList;
	
	public DefaultColumnMap(DefaultMutableTable table) {
		super(table.getEnvironment());
		this.table = table;
	}
	
	@Override
	public boolean add(DefaultMutableColumn c) {
		if (c.getTable() != this.table) {
			new IllegalArgumentException(
					"table of the column: " + c.getTable() + ", expected: " + this.table);
		}
		
		boolean added = super.add(c);
		
		if (added) {
			getColumnList().add(c);
		}
		
		return added;
	}

	List<DefaultMutableColumn> getColumnList() {
		if (columnList == null) {
			columnList = new ArrayList<DefaultMutableColumn>();			
		}

		return columnList;
	}
	
	public DefaultMutableColumn getColumn(int ordinal) {		
		return getColumnList().get(ordinal - 1);
	}
}
