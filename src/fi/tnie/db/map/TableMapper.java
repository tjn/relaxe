/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.map;

import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Schema;
import fi.tnie.db.meta.Table;

public interface TableMapper {
	
	enum Part {
		INTERFACE,		
		LITERAL_TABLE_ENUM,
		ABSTRACT,		
		HOOK,		
		IMPLEMENTATION,
		REF,
		FACTORY,
		METADATA
	}
				
	JavaType entityType(Table table, Part part);	
	
	AttributeInfo getAttributeInfo(Table table, Column c);
//	Class<?> getAttributeHolderType(Table table, Column c);
//	Class<?> getAttributeKeyType(Table table, Column c);
//	Class<?> getAttributeValueType(Table table, Column c);
//	Class<?> getAttributeType(Table table, Column c);
	JavaType factoryType(Schema schema, Part part);	
	JavaType catalogContextType();
	JavaType literalContextType();
	String getRootPackage();
	
	
}
