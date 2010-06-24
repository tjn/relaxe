/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.List;
import fi.tnie.db.expr.Identifier;

public interface PrimaryKey 
	extends Constraint {

	BaseTable getTable();		
	public List<? extends Column> columns();
	
	Column getColumn(Identifier name);
}
