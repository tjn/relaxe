/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.util.Properties;

import com.appspot.relaxe.ValueAssignerFactory;
import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.expr.SQLSyntax;
import com.appspot.relaxe.meta.SerializableEnvironment;

public interface Implementation<I extends Implementation<I>> {

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
	
	ValueExtractorFactory getValueExtractorFactory();			
	ValueAssignerFactory getValueAssignerFactory();
	
	String createJdbcUrl(String database);
	String createJdbcUrl(String host, String database);
	String createJdbcUrl(String host, int port, String database);
	
	SerializableEnvironment environment();
	
	I self();
	
	Properties getDefaultProperties();
	
}