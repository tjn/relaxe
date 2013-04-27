/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.expr.TableReference;
import com.appspot.relaxe.meta.ForeignKey;

public interface EntityQueryContext {

	public TableReference getReferenced(TableReference referencing, ForeignKey fk)
		throws EntityException;
}
