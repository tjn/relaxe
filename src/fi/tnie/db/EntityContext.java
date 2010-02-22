/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;


public interface EntityContext {
		 
	EntityMetaData<?, ?, ?, ?> getMetaData(BaseTable table);
	
	Catalog getCatalog();
}
