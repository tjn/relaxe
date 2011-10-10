/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;

import fi.tnie.db.expr.ColumnReference;
import fi.tnie.db.expr.Predicate;

public interface EntityQueryPredicate<A extends Attribute>
	extends Serializable {
	
	Predicate predicate(ColumnReference cr);
	
	/**
	 * May return <code>null</code> if this template does not require a column reference to be evaluated. 
	 *   
	 * @return
	 */
	A attribute();
}
