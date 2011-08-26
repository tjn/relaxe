/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;


public interface EntityBuildContext {

	DataObject.MetaData getInputMetaData();		
	EntityQuery<?, ?, ?, ?, ?> getQuery();
	AttributeWriterFactory getAttributeWriterFactory();	
	ColumnResolver getColumnResolver();
}
