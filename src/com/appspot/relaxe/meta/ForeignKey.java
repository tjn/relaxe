/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.Identifier;

public interface ForeignKey 
	extends Constraint {
	
	BaseTable getReferencing();
	BaseTable getReferenced();
	
	ColumnMap getColumnMap();		
		
	Column getReferenced(Column referencingColumn);
	Identifier getReferencedColumnName(Identifier referencingColumn);
}
