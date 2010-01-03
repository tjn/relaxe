/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.meta.PrimaryKey;

public class DefaultMutableColumn
	extends DefaultMutableMetaObject
	implements Column, Node<DefaultMutableTable>
{	
	private DefaultMutableTable table;
	
	private int nullable;
	private String remarks;
	private String columnDefault;
	private String isNullable;
	private String autoIncrement;
		
	private DataTypeImpl dataType;
	
	private Set<DefaultForeignKey> references;
	
	private NodeManager<DefaultMutableColumn, DefaultMutableTable> parentNodeManager;

	public DefaultMutableColumn() {
		super();
	}
	
	public DefaultMutableColumn(String n, DataTypeImpl type) {
		super(n);		
		setDataTypeImpl(type);
	}

	public DefaultMutableColumn(DefaultMutableTable t, String n, DataTypeImpl type) {
		this(n, type);
		setTable(t);		
	}

	@Override
	public DefaultMutableMetaObject getParent() {
		return getTable();
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

	private void setDataTypeImpl(DataTypeImpl dataType) {
		this.dataType = dataType;
	}
	
	public void detach() {
		this.table = null;
	}
	
	public void attach(DefaultMutableTable newTable) {
		this.table = newTable;
	}

	public DefaultMutableTable getTable() {
		return table;
	}

	public void setTable(DefaultMutableTable newTable) {		
		getParentNodeManager().setParent(this, newTable);
		this.table = newTable;
	}

	@Override
	public DefaultMutableTable getParentNode() {
		return this.table;
	}

	@Override
	public NodeManager<DefaultMutableColumn, DefaultMutableTable> getParentNodeManager() {
		DefaultMutableTable p = getTable();
		return (p == null) ? parentNodeManager : p.getNodeManager();
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
	
	
}
