/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.ForeignKey;

public interface EntityQueryContext {

	public TableReference getReferenced(TableReference referencing, ForeignKey fk)
		throws EntityException;
}
