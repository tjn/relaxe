/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.util;

import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Schema;

public interface CatalogVisitor {

	boolean visit(Schema s);	
	boolean visitBaseTables(Schema s);	
	boolean visitBaseTable(BaseTable t);
	boolean visitColumns(BaseTable t);
	void visit(Column c);
}
