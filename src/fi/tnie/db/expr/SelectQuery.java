/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public abstract class SelectQuery
	extends Query implements QueryExpression {
	
	abstract Select getSelect();			
}
