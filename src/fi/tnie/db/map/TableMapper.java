/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.map;

import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;

public interface TableMapper {
	
	enum Part {
		INTERFACE,		
		LITERAL_TABLE_ENUM,
		ABSTRACT,		
		HOOK,		
		IMPLEMENTATION,
		HAS,
		HAS_KEY,
		FACTORY,
		METADATA
	}
	
		
	JavaType entityType(Table table, Part part);	
		
	JavaType factoryType(Schema schema, Part part);	
	JavaType catalogContextType();
	JavaType literalContextType();
	String getRootPackage();
}
