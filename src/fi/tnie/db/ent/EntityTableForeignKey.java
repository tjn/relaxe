/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Map;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.AbstractImmutableForeignKey;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.ColumnMap;
import fi.tnie.db.types.ReferenceType;

public class EntityTableForeignKey
	extends AbstractImmutableForeignKey {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 8843482411504972590L;
	
	private ReferenceType<?, ?, ?, ?, ?, ?, ?, ?> type;
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected EntityTableForeignKey() {
	}
		
	
	protected EntityTableForeignKey(BaseTable table, Identifier name, ColumnMap columnMap, Map<Identifier, Identifier> columnPairMap, ReferenceType<?, ?, ?, ?, ?, ?, ?, ?> type) {
		super(table, name, columnMap, columnPairMap);
		this.type = type;
	}

	@Override
	public BaseTable getReferenced() {
		return type.getMetaData().getBaseTable();
	}
}
