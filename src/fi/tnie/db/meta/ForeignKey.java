/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.Map;

public interface ForeignKey 
	extends Constraint {
	
	BaseTable getReferencing();
	BaseTable getReferenced();
	
	public Map<Column, Column> columns();
}
