/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.meta.MetaObject;
import fi.tnie.db.meta.PrimaryKey;

public class DefaultMutableColumn
	implements Column, MetaObject
{	
	private DefaultMutableTable table;
	
	private int nullable;
	private String remarks;
	private String columnDefault;
	private String isNullable;
	private String autoIncrement;
		
	private DataTypeImpl dataType;	
	private Set<DefaultForeignKey> references;	
	private ColumnName columnName;	
	private int ordinalPosition;
		
	public DefaultMutableColumn() {
		super();
	}
	
	public DefaultMutableColumn(DefaultMutableTable t, Identifier name, DataTypeImpl type) {
		if (t == null || name == null || type == null) {
			throw new NullPointerException();
		}
		
		this.columnName = new ColumnName(name);
		this.table = t;
		t.add(this);
	}

	public int getNullable() {
		return nullable;
	}

	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getColumnDefault() {
		return columnDefault;
	}

	public void setColumnDefault(String columnDefault) {
		this.columnDefault = columnDefault;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public String getAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(String autoIncrement) {				
		this.autoIncrement = autoIncrement;
	}
	
	public DataTypeImpl getDataTypeImpl() {
		return dataType;
	}


	public DefaultMutableTable getTable() {
		return table;
	}
	
	public boolean add(DefaultForeignKey n) {		
		return getReferences().add(n);
	}
	
	public boolean remove(DefaultForeignKey k) {
		return getReferences().remove(k);
	}
	
	public Set<DefaultForeignKey> referencingForeignKeys() {
		if (references == null || references.isEmpty()) {
			return Collections.emptySet();
		}
		
		return Collections.unmodifiableSet(references); 
				
	}

	private Set<DefaultForeignKey> getReferences() {
		if (references == null) {
			references = new HashSet<DefaultForeignKey>();			
		}

		return references;
	}

	@Override
	public DataType getDataType() {		
		return getDataTypeImpl();
	}

	@Override
	public boolean isPrimaryKeyColumn() {
		DefaultMutableTable t = getTable();
		BaseTable b = (t == null) ? null : t.asBaseTable();
		PrimaryKey pk = (b == null) ? null : b.getPrimaryKey();							
		return (pk != null) && pk.columns().contains(this);
	}

	@Override
	public Boolean isAutoIncrement() {
		if (this.autoIncrement != null) {
			if ("YES".equals(this.autoIncrement)) {
				return Boolean.TRUE;
			}
	
			if ("NO".equals(this.autoIncrement)) {
				return Boolean.FALSE;
			}
		}
		
		if (autoIncrement == null || autoIncrement.equals("")) {
//			getDataTypeImpl().
		}
		
		// we just have an idea:
		return null;
	}

//	@Override
//	public ColumnName getColumnName() {
//		if (columnName == null) {
//			columnName = new ColumnName(getName(), true);			
//		}
//
//		return columnName;
//	}
//
//	@Override
//	public int ordinal() {
//		// TODO Auto-generated method stub
//		return 0;
//	}

	@Override
	public Identifier getUnqualifiedName() {
		return getColumnName();
	}

	@Override
	public ColumnName getColumnName() {
		return this.columnName;
	}

	public int getOrdinalPosition() {
		return ordinalPosition;
	}

	public void setOrdinalPosition(int ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}
}
