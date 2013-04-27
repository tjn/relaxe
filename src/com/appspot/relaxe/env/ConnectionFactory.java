/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public interface ConnectionFactory {

	Connection newConnection(String jdbcURL, Properties properties)
		throws SQLException; 
}
