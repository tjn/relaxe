/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;

public interface TableMapper {
	
	enum Part {
		INTERFACE,
		ABSTRACT,		
		HOOK,
		IMPLEMENTATION,
		FACTORY,
		METADATA
	}
				
	JavaType entityType(BaseTable table, Part part);	
	Class<?> getAttributeHolderType(Table table, Column c);
	Class<?> getAttributeType(Table table, Column c);
	JavaType factoryType(Schema schema, Part part);	
	JavaType catalogContextType();
	JavaType literalContextType();
	String getRootPackage();
}
