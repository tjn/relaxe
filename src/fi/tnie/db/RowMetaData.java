/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.expr.ValueExpression;

public interface RowMetaData {	
	int getColumnCount();
	ValueExpression getColumnExpr(int column);		
}
