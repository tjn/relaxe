/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.feature.SQLGenerationException;
import fi.tnie.db.query.QueryException;

public abstract class Tool {

	private static Logger logger = Logger.getLogger(Tool.class);
	
	public void run(Implementation env, String jdbcUrl, Properties jdbcConfig) 
		throws Exception {
	        		
	    String driverName = env.defaultDriverClassName();
	    logger().debug("loading " + driverName);
		Class<?> driverClass = Class.forName(driverName);
		logger().debug("driver loaded.");
		
		Driver selected = null;
		
		List<Driver> loaded = Collections.list(DriverManager.getDrivers());
		
		for(Driver d : loaded) {
			if (d.getClass().equals(driverClass)) {
				selected = d;
				break;
			}				
		}
		
		if (!selected.acceptsURL(jdbcUrl)) {
			throw new IllegalArgumentException(
					"Driver " + selected.getClass() + " does not accept URL: " + jdbcUrl);
		}
		
		logger().debug("connecting to: " + jdbcUrl);
		
		Connection c = selected.connect(jdbcUrl, jdbcConfig);
				
		logger().debug("connected.");
		
		if (c == null) {
			throw new IllegalArgumentException("can not create connection to " + jdbcUrl);
		}
		
		try {
			run(env, c);
		}
		finally {
		    close(c);		    
		}		
	}

	protected void close(Connection c) {
	    if (c != null) {
	        try {
                c.close();
            } 
	        catch (SQLException e) {
	            logger().warn(e.getMessage());
            }
	    }
    }

    public abstract void run(Implementation env, Connection c)
		throws QueryException, IOException, SQLGenerationException, SQLException;

	public static Logger logger() {
		return Tool.logger;
	}
}
