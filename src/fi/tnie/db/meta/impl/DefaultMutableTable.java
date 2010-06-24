/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.List;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

public abstract class DefaultMutableTable
	extends DefaultSchemaElement
	implements Table {
	
	private ColumnMap columnMap;
			
	DefaultMutableTable(DefaultMutableSchema s, Identifier name) {
		super(s, name);		
		s.add(this);
	}	
		
	public boolean add(DefaultMutableColumn c) {
		return getColumnMap().add(c);
	}
	

	public DefaultMutableBaseTable asBaseTable() {
		return (DefaultMutableBaseTable) (isBaseTable() ? this : null);
	}

	ColumnMap getColumnMap() {
		if (columnMap == null) {
			columnMap = new ColumnMap(this);			
		}

		return columnMap;
	}

	@Override
	public Column getColumn(Identifier cn) {		
		return getColumnMap().get(cn);
	}

	@Override
	public ColumnMap columnMap() {		
		return getColumnMap();
	}
	
	@Override
	public List<? extends Column> columns() {	
		return getColumnMap().getColumnList();
	}	
	
}
