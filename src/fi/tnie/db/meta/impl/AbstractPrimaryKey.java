/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Collections;
import java.util.List;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SchemaElementName;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Constraint;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.PrimaryKey;

public abstract class AbstractPrimaryKey
	implements PrimaryKey
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2813449313924403527L;
	
	private Identifier constraintName;
	private Identifier columnName;
	private transient SchemaElementName qualifiedName;
			
	protected AbstractPrimaryKey() {
		super();
	}
	
	public AbstractPrimaryKey(Identifier constraintName, Identifier columnName) {
		this.constraintName = constraintName;
		this.columnName = columnName;				
	}
	
	@Override
	public Environment getEnvironment() {
		return getTable().getEnvironment();
	}

	@Override
	public List<? extends Column> columns() {
		return Collections.singletonList(getColumn());
	}
	
	protected Column getColumn() {
		return getTable().getColumn(columnName);
	}

	@Override
	public abstract Column getColumn(Identifier name);

	@Override
	public abstract BaseTable getTable();

	@Override
	public Type getType() {
		return Constraint.Type.PRIMARY_KEY;
	}

	@Override
	public SchemaElementName getName() {
		if (qualifiedName == null) {
			qualifiedName = new SchemaElementName(getTable().getName().getQualifier(), this.constraintName);			
		}

		return qualifiedName;
	}

	@Override
	public String getQualifiedName() {
		return getName().generate();
	}

	@Override
	public Identifier getUnqualifiedName() {
		return this.constraintName;
	}

	
}
