/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;

public class ImmutableColumn
	implements Column
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2813449313924403527L;
	
	private boolean definitelyNotNullable;
	private String remarks;
	private String columnDefault;
	private String autoIncrement;
		
	private DataTypeImpl dataType;	
	private ColumnName columnName;	
	private int ordinalPosition;
	private boolean primaryKeyColumn;
			
	/**
	 * Same as DatabaseMetaData.columnNoNulls
	 */
//	private static final int columnNoNulls = 0;
		
	protected ImmutableColumn() {
		super();
	}
	
	public ImmutableColumn(int ordinalPosition, Identifier name, DataTypeImpl type, boolean primaryKeyColumn, Boolean autoIncrement, String remarks, boolean definitelyNotNullable, String columnDefault) {		
		this.columnName = new ColumnName(name);
		this.ordinalPosition = ordinalPosition;
		this.dataType = type;
		this.remarks = remarks;
		this.primaryKeyColumn = primaryKeyColumn;		
		this.definitelyNotNullable = definitelyNotNullable;
		this.columnDefault = columnDefault;
		
		String ai = (autoIncrement == null) ? null : (autoIncrement.booleanValue() ? "YES" : "NO");		
		this.autoIncrement = ai;
	}

	public String getRemarks() {
		return remarks;
	}

	public String getColumnDefault() {
		return columnDefault;
	}

	public void setColumnDefault(String columnDefault) {
		this.columnDefault = columnDefault;
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

	@Override
	public DataType getDataType() {		
		return getDataTypeImpl();
	}

	@Override
	public boolean isPrimaryKeyColumn() {				
		return this.primaryKeyColumn;
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
		
		// we just have no idea:
		return null;
	}

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
	
	@Override
	public String toString() {		
		return getColumnName().getName();
	}

    public boolean isDefinitelyNotNullable() {
        return this.definitelyNotNullable;
    }
	
}
