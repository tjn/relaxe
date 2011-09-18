/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

import fi.tnie.db.meta.Column;

/**
 * Represents the SQL DEFAULT -value expression in 
 * VALUES -clause of the INSERT -statement and the right-hand side of the assignment in UPDATE -statement.
 * 
 * <sql>
 * 	INSERT INTO DEFAULT_TEST VALUES (DEFAULT, DEFAULT)
 * </sql>
 * 
 *<sql>
 * 	UPDATE DEFAULT_TEST SET NAME = DEFAULT
 * </sql>
 * 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */

public class Default
	implements ValueExpression {
	
	private Column column;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected Default() {
	}

	public Default(Column col) {
		super();
		this.column = col;
	}

	@Override
	public ColumnName getColumnName() {
		return null;
	}

	@Override
	public int getType() {
		return this.column.getDataType().getDataType();
	}

	@Override
	public String getTerminalSymbol() {
		return null;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		Keyword kw = Keyword.DEFAULT;
		kw.traverse(vc, v);
	}
	
	
	
}
