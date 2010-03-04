/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;
import fi.tnie.db.meta.SchemaElementMap;

public class DefaultMutableBaseTable extends DefaultMutableTable 
	implements BaseTable {
	
	private DefaultSchemaElementMap<ForeignKey> foreignKeys;
	private DefaultSchemaElementMap<ForeignKey> referencingKeys;	
	private DefaultPrimaryKey primaryKey;	
	
	private static EmptyForeignKeyMap emptyForeignKeyMap = new EmptyForeignKeyMap();
	
	public DefaultMutableBaseTable(DefaultMutableSchema s, Identifier name) {
		super(s, name);	
	}
	

	void add(DefaultForeignKey k) {
		if (k.getReferencing() != this) {
			throw new IllegalArgumentException(
					"Can not add a foreign key from the other table: " + k.getReferencing());
		}
		
		getForeignKeys().add(k);
		getMutableSchema().add(k);
	}
	
	
	void ref(DefaultForeignKey k) {
		if (k.getReferenced() != this) {
			throw new IllegalArgumentException(
					"Can not add a reference from the foreign key referencing to the other table: " + k.getReferenced());
		}	
		
		getReferencingKeys().add(k);
	}
	
	void setPrimaryKey(DefaultPrimaryKey pk) {
		if (pk != null) {
			if (pk.getTable() != this) {
				throw new IllegalArgumentException(
						"Can not add a primary key from the other table: " + pk.getTable());
			}
			
			if (pk.getName() == null) {
				throw new NullPointerException("'k.getName()' must not be null");
			}
		}
		
		this.primaryKey = pk;
		getMutableSchema().add(pk);
	}	
	
	public PrimaryKey getPrimaryKey() {
		return this.primaryKey;
	}

	


	private DefaultSchemaElementMap<ForeignKey> getForeignKeys() {
		if (foreignKeys == null) {
						
			foreignKeys = createElementMap();
		}

		return foreignKeys;
	}

	private DefaultSchemaElementMap<ForeignKey> getReferencingKeys() {
		if (referencingKeys == null) {
			referencingKeys = createElementMap();						
		}

		return referencingKeys;
	}

//	@Override
//	public Map<String, ForeignKey> foreignKeys() {
//		if (foreignKeys == null) {
//			return Collections.emptyMap();
//		}
//		
//		return Collections.unmodifiableMap(foreignKeys);
//	}

	@Override
	public String getTableType() {
		return BASE_TABLE;
	}

	@Override
	public boolean isBaseTable() {
		return true;
	}
	
	@Override
	public String toString() {		
		return "base table " + getQualifiedName() + "@" + System.identityHashCode(this);
	}

	@Override
	public SchemaElementMap<ForeignKey> foreignKeys() {
		return (foreignKeys == null) ? DefaultMutableBaseTable.emptyForeignKeyMap : foreignKeys;
	}

	@Override
	public SchemaElementMap<ForeignKey> references() {
		return this.referencingKeys;
	}
	
//	private Map<String, Column> getPrimaryKeyColumns() {
//		if (primaryKeyColumns == null) {
//			primaryKeyColumns = new LinkedHashMap<String, Column>();			
//		}
//
//		return primaryKeyColumns;
//	}
//
//	@Override
//	public List<Column> primaryKey() {
//		if (primaryKeyColumns == null || primaryKeyColumns.isEmpty()) {
//			throw new IllegalStateException(
//					"primary key is not defined for the table " + getQualifiedName());
//		}
//		
//		return new ArrayList<Column>(primaryKeyColumns.values());
//	}
}
