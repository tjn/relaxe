/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;

public class DefaultMutableBaseTable extends DefaultMutableTable 
	implements BaseTable {
		
	private Map<String, ForeignKey> foreignKeys;	
	private Map<String, ForeignKey> referencingKeys;
			
//	private Map<String, Column> primaryKeyColumns;
	
	private DefaultPrimaryKey primaryKey;	
	
	public DefaultMutableBaseTable(DefaultMutableSchema s, String name) {
		super(s, name);	
	}

	void add(DefaultForeignKey k) {
		if (k.getReferencing() != this) {
			throw new IllegalArgumentException(
					"Can not add a foreign key from the other table: " + k.getReferencing());
		}
		
		if (k.getName() == null) {
			throw new NullPointerException("'k.getName()' must not be null");
		}
		
		getForeignKeys().put(k.getName(), k);
	}
	
	
	void ref(DefaultForeignKey k) {
		if (k.getReferenced() != this) {
			throw new IllegalArgumentException(
					"Can not add a foreign key from the other table: " + k.getReferenced());
		}
		
		if (k.getName() == null) {
			throw new NullPointerException("'k.getName()' must not be null");
		}
		
		getReferencingKeys().put(k.getName(), k);
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
	}	
	
	public PrimaryKey getPrimaryKey() {
		return this.primaryKey;
	}
	
	 
	
//	void addPrimaryKeyColumn(DefaultMutableColumn c) {
//		if (c.getParentNode() != this) {
//			throw new IllegalArgumentException(
//					"Can not add a primary key column from the other table: " + c.getParentNode());
//		}
//		
//		if (c.getName() == null) {
//			throw new NullPointerException("'c.getName()' must not be null");
//		}
//		
//		getPrimaryKeyColumns().put(c.getName(), c);
//	}
	
	
	
	
	
	
	@Override
	public Map<String, ForeignKey> references() {
		if (referencingKeys == null) {
			return Collections.emptyMap();
		}
		
		return Collections.unmodifiableMap(referencingKeys);
	}

	private Map<String, ForeignKey> getForeignKeys() {
		if (foreignKeys == null) {
			foreignKeys = new HashMap<String, ForeignKey>();			
		}

		return foreignKeys;
	}

	private Map<String, ForeignKey> getReferencingKeys() {
		if (referencingKeys == null) {
			referencingKeys = new HashMap<String, ForeignKey>();						
		}

		return referencingKeys;
	}

	@Override
	public Map<String, ForeignKey> foreignKeys() {
		if (foreignKeys == null) {
			return Collections.emptyMap();
		}
		
		return Collections.unmodifiableMap(foreignKeys);
	}

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
