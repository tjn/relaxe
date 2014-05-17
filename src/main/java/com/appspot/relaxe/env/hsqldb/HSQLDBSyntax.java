package com.appspot.relaxe.env.hsqldb;

import com.appspot.relaxe.env.hsqldb.expr.HSQLDBArrayTypeDefinition;
import com.appspot.relaxe.expr.DefaultSQLSyntax;
import com.appspot.relaxe.expr.ddl.types.SQLDataType;

public class HSQLDBSyntax
    extends DefaultSQLSyntax {
	
	@Override
	public SQLDataType newArrayType(SQLDataType elementType) {
		return new HSQLDBArrayTypeDefinition(elementType, null);
	}
}