/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import fi.tnie.db.expr.Identifier;

public interface ForeignKey 
	extends Constraint {
	
	BaseTable getReferencing();
	BaseTable getReferenced();
	
	ColumnMap getColumnMap();		
		
	Column getReferenced(Column referencingColumn);
	Identifier getReferencedColumnName(Identifier referencingColumn);
}
