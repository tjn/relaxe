/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;



public interface QueryContext {

	OrdinaryIdentifier correlationName(NonJoinedTable tref);	
	Identifier generateColumnName(ValueElement e);

}