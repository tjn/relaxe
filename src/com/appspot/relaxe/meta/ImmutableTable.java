/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;

public abstract class ImmutableTable
	extends ImmutableSchemaElement
	implements Table
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8419855038848688801L;
	private ColumnMap columnMap;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ImmutableTable() {
	}
		
	protected ImmutableTable(Environment environment, SchemaElementName name, ColumnMap columnMap) {
		super(environment, name);
		this.columnMap = columnMap;	
	}

	public static abstract class Builder<T extends Table>
		extends MetaObjectBuilder {
				
		private SchemaElementName name;
		
		private ImmutableColumnMap.Builder columnMapBuilder; 
			
		public Builder(Environment environment, SchemaElementName sen) {
			super(environment);			
			this.columnMapBuilder = new ImmutableColumnMap.Builder(environment);
			this.name = sen;
		}
		
		public SchemaElementName getName() {
			return this.name;
		}
		
		public void add(Column c) {
			this.columnMapBuilder.add(c);
		}		
		
		public Column getColumn(Identifier name) {
			return this.columnMapBuilder.getColumn(name);			
		}
		
		protected ImmutableColumnMap.Builder getColumnMapBuilder() {
			return columnMapBuilder;
		}
		
		public void add(Identifier name, int ordinalPosition, DataType type, Boolean autoIncrement, String remarks, boolean definitelyNotNullable, String columnDefault) {
			this.columnMapBuilder.add(name, ordinalPosition, type, autoIncrement, remarks, definitelyNotNullable, columnDefault);
		}						
		
//		public T newTable() {
//			if (this.name.getQualifier() == null) {
//				throw new IllegalStateException("schema is not set");
//			} 						
//			
//			if (this.name.getUnqualifiedName() == null) {
//				throw new IllegalStateException("table name is not set");
//			}
//			
//			if (this.columnMapBuilder.getColumnCount() == 0) {
//				throw new IllegalStateException("can not create table without columns");
//			}
//			
//			ColumnMap cm = this.columnMapBuilder.newColumnMap();
//			
//			T table = newTable(this.schemaName, tableName, cm);
//						
//			return table;
//		}
//
//		protected abstract T newTable(SchemaName schema, Identifier tableName, ColumnMap columnMap);
		
	}

	@Override
	public ColumnMap columnMap() {
		return this.columnMap;
	}
	
	@Override
	public abstract boolean isBaseTable();
}
