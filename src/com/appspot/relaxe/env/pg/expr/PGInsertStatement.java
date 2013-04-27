/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.pg.expr;

import com.appspot.relaxe.expr.ColumnName;
import com.appspot.relaxe.expr.ElementList;
import com.appspot.relaxe.expr.InsertStatement;
import com.appspot.relaxe.meta.Table;

public class PGInsertStatement
	extends InsertStatement {

	public PGInsertStatement(Table target, ElementList<ColumnName> columnNameList) {
		super(target, columnNameList);	
	}

}
