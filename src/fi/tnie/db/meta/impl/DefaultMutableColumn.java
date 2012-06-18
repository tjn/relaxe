/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

//import java.sql.DatabaseMetaData;
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 2813449313924403527L;

	private DefaultMutableTable table;
	
	private boolean definitelyNotNullable;
	private String remarks;
	private String columnDefault;
	private String autoIncrement;
		
	private DataTypeImpl dataType;	
	private Set<DefaultForeignKey> references;	
	private ColumnName columnName;	
	private int ordinalPosition;	
			
	/**
	 * Same as DatabaseMetaData.columnNoNulls
	 */
	private static final int columnNoNulls = 0;
		
	public DefaultMutableColumn() {
		super();
	}
	
	public DefaultMutableColumn(DefaultMutableTable t, Identifier name, DataTypeImpl type, String autoIncrement) {
		this(t, name, type);
		this.autoIncrement = autoIncrement;
	}
	
	public DefaultMutableColumn(DefaultMutableTable t, Identifier name, DataTypeImpl type, Boolean autoIncrement) {
		this(t, name, type);
		String ai = (autoIncrement == null) ? null : (autoIncrement.booleanValue() ? "YES" : "NO");		
		this.autoIncrement = ai;
	}
	
	private DefaultMutableColumn(DefaultMutableTable t, Identifier name, DataTypeImpl type) {
		if (t == null || name == null || type == null) {
			throw new NullPointerException();
		}
						
		this.columnName = new ColumnName(name);
		this.table = t;
		
		this.dataType = new DataTypeImpl(type);		
		t.add(this);
	}

//	public Boolean getNullable() {
//		return nullable;
//	}

	public void setNullable(int nullability) {						
	    this.definitelyNotNullable = (nullability == columnNoNulls);
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

	public void setOrdinalPosition(int ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}
	
	@Override
	public String toString() {		
		return getTable().getQualifiedName() + "." + getColumnName().getName();
	}

    public boolean isDefinitelyNotNullable() {
        return definitelyNotNullable;
    }
	
}
