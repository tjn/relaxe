/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;



public interface QueryContext {

	OrdinaryIdentifier correlationName(NonJoinedTable tref);	
	Identifier generateColumnName(ValueElement e);

}