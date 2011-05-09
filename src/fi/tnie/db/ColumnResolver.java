/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.meta.Column;

public interface ColumnResolver {
	public Column getColumn(int index);
}
