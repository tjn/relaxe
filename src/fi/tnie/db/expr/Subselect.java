/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.expr;

public interface Subselect
	extends Expression {
	
	Select getSelect();
}
