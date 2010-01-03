/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.List;

public interface PrimaryKey 
	extends Constraint {

	BaseTable getTable();		
	public List<Column> columns();
}
