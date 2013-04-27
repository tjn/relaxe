/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.util.Map;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.meta.AbstractImmutableForeignKey;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.types.ReferenceType;


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
