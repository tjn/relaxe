/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.meta.impl.hsqldb;

import fi.tnie.db.expr.ddl.ColumnDefinition;
import fi.tnie.db.meta.SerializableEnvironment;
import fi.tnie.db.meta.impl.DefaultEnvironment;

public class HSQLDBEnvironment 
	extends DefaultEnvironment
	implements SerializableEnvironment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2968383567147338099L;

	
	public ColumnDefinition serialColumnDefinition(String columnName, boolean big) {
		return null;		
//		Identifier n = createIdentifier(columnName);                
//        return new ColumnDefinition(n, new Serial());
	}
	
}