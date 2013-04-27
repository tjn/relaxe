/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Map;

import com.appspot.relaxe.expr.Identifier;

public abstract class AbstractImmutableForeignKey
	extends ImmutableConstraint
	implements ForeignKey {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6311988355706864990L;
	
	private ColumnMap columnMap;
	private Map<Identifier, Identifier> columnPairMap;
	
	protected AbstractImmutableForeignKey() {
		super();	
	}

	protected AbstractImmutableForeignKey(BaseTable table, Identifier name, ColumnMap columnMap, Map<Identifier, Identifier> columnPairMap) {
		super(table, name);	
		this.columnMap = columnMap;	
		this.columnPairMap = columnPairMap;
	}

	@Override
	public Type getType() {
		return Type.FOREIGN_KEY;
	}
	
	@Override
	public Identifier getReferencedColumnName(Identifier referencingColumn) {
		if (referencingColumn == null) {
			return null;
		}
		
		return this.columnPairMap.get(referencingColumn);		
	}
	
	@Override
	public Column getReferenced(Column referencingColumn) {
		if (referencingColumn == null) {
			return null;
		}
				
		Identifier cn = getReferencedColumnName(referencingColumn.getUnqualifiedName());
		ColumnMap cm = getReferenced().columnMap();
		Column rc = cm.get(cn);
		return rc;
	}


	@Override
	public ColumnMap getColumnMap() {
		return this.columnMap;
	}
	
	@Override
	public abstract BaseTable getReferenced();


	@Override
	public BaseTable getReferencing() {
		return getTable();
	}
}
