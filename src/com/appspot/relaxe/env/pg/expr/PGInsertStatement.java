/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg.expr;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.InsertStatement;
import com.appspot.relaxe.meta.Table;

public class PGInsertStatement
	extends InsertStatement {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5899030048028001816L;

	public PGInsertStatement(Table target, ElementList<Identifier> columnNameList) {
		super(target, columnNameList);	
	}

}
