/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;


public interface EntityBuildContext {

	DataObject.MetaData getInputMetaData();		
	EntityQuery<?, ?, ?, ?, ?, ?, ?, ?, ?> getQuery();
//	AttributeWriterFactory getAttributeWriterFactory();	
	ColumnResolver getColumnResolver();
		
}