/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.meta;


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
	private Identifier columnName;	
	private int ordinalPosition;
	
			
	/**
	 * Same as DatabaseMetaData.columnNoNulls
	 */
//	private static final int columnNoNulls = 0;
		
	protected ImmutableColumn() {
		super();
	}
	
	public ImmutableColumn(Identifier name, int ordinalPosition, DataType type, Boolean autoIncrement, String remarks, boolean definitelyNotNullable, String columnDefault) {		
		this.columnName = name;
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

	@Override
	public String getColumnDefault() {
		return columnDefault;
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
		return this.columnName;
	}

	@Override
	public Identifier getColumnName() {
		return this.columnName;
	}

	public int getOrdinalPosition() {
		return ordinalPosition;
	}
	
	@Override
	public String toString() {		
		return getColumnName().getContent();
	}

    @Override
	public boolean isDefinitelyNotNullable() {
        return this.definitelyNotNullable;
    }

}
