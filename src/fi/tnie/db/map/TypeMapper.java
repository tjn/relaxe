/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.map;

import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

public interface TypeMapper {

	AttributeInfo getAttributeInfo(Table table, Column c);
}
