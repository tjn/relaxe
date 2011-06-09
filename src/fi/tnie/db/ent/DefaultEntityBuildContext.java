/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.AttributeWriterFactory;
import fi.tnie.db.ColumnResolver;
import fi.tnie.db.ent.DataObject.MetaData;

public class DefaultEntityBuildContext 
	implements EntityBuildContext {

	private AttributeWriterFactory attributeWriterFactory;
	private ColumnResolver columnResolver;
	private DataObject.MetaData inputMetaData;
	private EntityQuery<?, ?, ?, ?, ?> query;
		
	public DefaultEntityBuildContext(
			MetaData inputMetaData,
			EntityQuery<?, ?, ?, ?, ?> query, 
			AttributeWriterFactory attributeWriterFactory,
			ColumnResolver columnResolver) {
		super();
		this.inputMetaData = inputMetaData;
		this.query = query;
		this.attributeWriterFactory = attributeWriterFactory;
		this.columnResolver = columnResolver;		
	}
	public AttributeWriterFactory getAttributeWriterFactory() {
		return attributeWriterFactory;
	}
	
	public ColumnResolver getColumnResolver() {
		return columnResolver;
	}
	
	public DataObject.MetaData getInputMetaData() {
		return inputMetaData;
	}
	
	public EntityQuery<?, ?, ?, ?, ?> getQuery() {
		return query;
	}
}
