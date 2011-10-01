/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import java.sql.Driver;

import fi.tnie.db.ValueAssignerFactory;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.expr.SQLSyntax;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.SerializableEnvironment;

public interface Implementation
	extends Environment {

	/** Creates a factory to build entire catalog in this environment. 
	 * 
	 * @return
	 */
	CatalogFactory catalogFactory();
	String driverClassName();
	SQLSyntax getSyntax();

	
	GeneratedKeyHandler generatedKeyHandler();
	ValueExtractorFactory getValueExtractorFactory();			
	ValueAssignerFactory getValueAssignerFactory();
	
	String createJdbcUrl(String database);
	String createJdbcUrl(String host, String database);
	String createJdbcUrl(String host, int port, String database);
	
	SerializableEnvironment environment();
	
	Driver getDriver();		
}