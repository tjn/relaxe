/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import fi.tnie.db.ValueAssignerFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.SerializableEnvironment;

public interface Implementation
	extends Environment, PersistenceContext {

	/** Creates a factory to build entire catalog in this environment. 
	 * 
	 * @return
	 */
	CatalogFactory catalogFactory();
	
	/**
	 * Returns fully qualified class name of the JDBC driver for this implementation. 
	 * 
	 * @return
	 */
	String defaultDriverClassName();
	SQLSyntax getSyntax();
	
	GeneratedKeyHandler generatedKeyHandler();
//	AttributeWriterFactory getAttributeWriterFactory();
	ValueExtractorFactory getValueExtractorFactory();			
	ValueAssignerFactory getValueAssignerFactory();
	
	String createJdbcUrl(String database);
	String createJdbcUrl(String host, String database);
	String createJdbcUrl(String host, int port, String database);
	
	SerializableEnvironment environment();
	
//	Driver getDriver();			
}