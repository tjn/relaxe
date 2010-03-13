/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.File;
import java.util.Map;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;
import fi.tnie.db.source.JavaType;

public interface TableMapper {
	
	enum Part {
		INTERFACE,
		ABSTRACT,		
		HOOK,
		IMPLEMENTATION,
		METADATA
	}
	
	File getSourceDir(BaseTable table, Part part);		
	JavaType entityType(BaseTable table, Part part);	
	Class<?> getAttributeType(Table table, Column c);
	
	JavaType factoryType(Schema schema, Part part);			
}
