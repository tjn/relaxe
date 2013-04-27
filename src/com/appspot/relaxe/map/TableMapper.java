/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.map;

import com.appspot.relaxe.meta.Schema;
import com.appspot.relaxe.meta.Table;

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
