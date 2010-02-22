/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.util;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Schema;

public interface CatalogVisitor {

	boolean visit(Schema s);	
	boolean visitBaseTables(Schema s);	
	boolean visitBaseTable(BaseTable t);
	boolean visitColumns(BaseTable t);
	void visit(Column c);
}
