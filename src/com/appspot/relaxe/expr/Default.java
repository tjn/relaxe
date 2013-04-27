/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import com.appspot.relaxe.meta.Column;

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
	implements ValuesListElement {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7419094016483767270L;
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
	public String getTerminalSymbol() {
		return null;
	}
	
	public Column getColumn() {
		return column;
	}

	@Override
	public void traverse(VisitContext vc, ElementVisitor v) {
		SQLKeyword kw = SQLKeyword.DEFAULT;
		kw.traverse(vc, v);
	}
	
	
	
}
