/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import com.appspot.relaxe.ent.ColumnResolver;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.EntityBuildContext;
import com.appspot.relaxe.ent.EntityQuery;
import com.appspot.relaxe.ent.DataObject.MetaData;

public class DefaultEntityBuildContext 
	implements EntityBuildContext {

//	private AttributeWriterFactory attributeWriterFactory;
	private ColumnResolver columnResolver;
	private DataObject.MetaData inputMetaData;
	private EntityQuery<?, ?, ?, ?, ?, ?, ?, ?, ?> query;
		
	public DefaultEntityBuildContext(
			MetaData inputMetaData,
			EntityQuery<?, ?, ?, ?, ?, ?, ?, ?, ?> query,
			
			ColumnResolver columnResolver) {
		super();
		this.inputMetaData = inputMetaData;
		this.query = query;		
		this.columnResolver = columnResolver;		
	}
	
//	public AttributeWriterFactory getAttributeWriterFactory() {
//		return attributeWriterFactory;
//	}
	
	public ColumnResolver getColumnResolver() {
		return columnResolver;
	}
	
	public DataObject.MetaData getInputMetaData() {
		return inputMetaData;
	}
	
	public EntityQuery<?, ?, ?, ?, ?, ?, ?, ?, ?> getQuery() {
		return query;
	}
}
