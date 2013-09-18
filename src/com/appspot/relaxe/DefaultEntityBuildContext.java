/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import com.appspot.relaxe.ent.ColumnResolver;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.EntityBuildContext;
import com.appspot.relaxe.ent.DataObject.MetaData;
import com.appspot.relaxe.ent.EntityQueryContext;

public class DefaultEntityBuildContext 
	implements EntityBuildContext {

	private ColumnResolver columnResolver;
	private DataObject.MetaData inputMetaData;
	private EntityQueryContext queryContext;
		
	public DefaultEntityBuildContext(
			MetaData inputMetaData,			
			EntityQueryContext queryContext, 			
			ColumnResolver columnResolver) {
		super();
		this.inputMetaData = inputMetaData;
		this.queryContext = queryContext;			
		this.columnResolver = columnResolver;		
	}
	
	@Override
	public ColumnResolver getColumnResolver() {
		return columnResolver;
	}
	
	@Override
	public DataObject.MetaData getInputMetaData() {
		return inputMetaData;
	}
	
	@Override
	public EntityQueryContext getQueryContext() {
		return this.queryContext;
	}
}
