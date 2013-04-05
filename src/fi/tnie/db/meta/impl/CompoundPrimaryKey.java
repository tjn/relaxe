/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Constraint;
import fi.tnie.db.meta.FoldingComparator;
import fi.tnie.db.meta.PrimaryKey;

public abstract class CompoundPrimaryKey
	implements PrimaryKey
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2813449313924403527L;
	
	private Identifier constraintName;
	private Column column;	
	private BaseTable table;
	private SchemaElementName qualifiedName;
	private Identifier[] columnNames;
	private Comparator<Identifier> comparator;
				
	protected CompoundPrimaryKey() {
		super();
	}
	
	public CompoundPrimaryKey(BaseTable table, Comparator<Identifier> comparator, Identifier constraintName, Identifier ... columnNames) {		
		this.table = table;
		this.constraintName = constraintName;
				
		for (Identifier cn : columnNames) {
			
		}
		
		
//		this.column = table.getColumn(columnName);
		this.qualifiedName = new SchemaElementName(table.getName().getQualifier(), constraintName);		
	}

	@Override
	public List<? extends Column> columns() {
		return Collections.singletonList(this.column);
	}
	

	@Override
	public Column getColumn(Identifier name) {
		return null;
	}

	@Override
	public BaseTable getTable() {
		return this.table;
	}

	@Override
	public Type getType() {
		return Constraint.Type.PRIMARY_KEY;
	}

	@Override
	public SchemaElementName getName() {
		return this.qualifiedName;
	}

	@Override
	public String getQualifiedName() {
		return this.qualifiedName.generate();
	}

	@Override
	public Identifier getUnqualifiedName() {
		return this.constraintName;
	}

	
}
