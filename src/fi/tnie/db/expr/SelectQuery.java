/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public abstract class SelectQuery
	extends QueryExpression {
	
	abstract Select getSelect();			
}
