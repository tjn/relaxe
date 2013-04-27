/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.ColumnName;
import com.appspot.relaxe.expr.Identifier;

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
		
	private DataType dataType;	
	private ColumnName columnName;	
	private int ordinalPosition;
	
			
	/**
	 * Same as DatabaseMetaData.columnNoNulls
	 */
//	private static final int columnNoNulls = 0;
		
	protected ImmutableColumn() {
		super();
	}
	
	public ImmutableColumn(Identifier name, int ordinalPosition, DataType type, Boolean autoIncrement, String remarks, boolean definitelyNotNullable, String columnDefault) {		
		this.columnName = new ColumnName(name);
		this.ordinalPosition = ordinalPosition;
		this.dataType = type;
		this.remarks = remarks;				
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
	
//	public DataType getDataTypeImpl() {
//		return dataType;
//	}

	@Override
	public DataType getDataType() {		
		return this.dataType;
	}

//	@Override
//	public boolean isPrimaryKeyColumn() {				
//		return this.primaryKeyColumn;
//	}

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
