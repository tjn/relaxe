/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.util;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import fi.tnie.db.QueryException;
import fi.tnie.db.feature.SQLGenerationException;
import fi.tnie.util.io.IOHelper;

public abstract class Tool {

	private static Logger logger = Logger.getLogger(Tool.class);
	
	public void run(String[] args) 
		throws Exception {
		
		if (args.length < 3) {
			throw new IllegalArgumentException(
					"usage:\n" +
					"java " + getClass().getName() + " <driver-class> <url> <config-file>"					
			);
		}			
		
		String driverName = args[0];
		String url = args[1];
		String cfg = args[2];
							
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
		
		if (!selected.acceptsURL(url)) {
			throw new IllegalArgumentException(
					"driver " + selected.getClass() + " does not accept URL: " + url);
		}
		
		logger().debug("loading config: " + new File(cfg).getAbsolutePath());					
		Properties info = IOHelper.load(cfg);
		logger().debug("config loaded.");
										
		logger().debug("connecting to: " + url);
		
		Connection c = selected.connect(url, info);
				
		logger().debug("connected.");
		
		if (c == null) {
			throw new IllegalArgumentException("can not create connection to " + url);
		}
		
		try {
			run(c, info);
		}
		finally {
			try {
				c.close();
			}
			catch (SQLException e) {
				logger().error(e.getMessage(), e);
			}
		}		
	}

	public abstract void run(Connection c, Properties config)
		throws QueryException, IOException, SQLGenerationException, SQLException;

	public static Logger logger() {
		return Tool.logger;
	}
}
