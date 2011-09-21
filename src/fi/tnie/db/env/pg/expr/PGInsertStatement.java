/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.pg.expr;

import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.InsertStatement;
import fi.tnie.db.meta.Table;

public class PGInsertStatement
	extends InsertStatement {

	public PGInsertStatement(Table target, ElementList<ColumnName> columnNameList) {
		super(target, columnNameList);	
	}

}
