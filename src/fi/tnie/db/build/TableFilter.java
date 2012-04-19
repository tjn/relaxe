/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.build;

import fi.tnie.db.meta.Schema;

public interface TableFilter {
	boolean accept(Schema s);
}
