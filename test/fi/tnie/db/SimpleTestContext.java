/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.query.QueryException;

/**
 * @author Administrator
 *
 */
public class SimpleTestContext
    implements EnvironmentTestContext, TestContext {
	
	private static Logger logger = Logger.getLogger(SimpleTestContext.class);

    private Catalog catalog;
    private Implementation implementation;
    private Driver driver;
    private String jdbcURL;
    private Properties driverConfig;
            
    public SimpleTestContext(Implementation impl, Driver driver, String jdbcURL, Properties driverConfig)
    	throws ClassNotFoundException {            
        super();
        this.implementation = impl;            
        this.driver = driver;
        this.jdbcURL = jdbcURL;
        this.driverConfig = driverConfig;
    }
    public SimpleTestContext(Implementation impl) {
    	this(impl, null, "test", "test", "test");
    }
    
    public SimpleTestContext(Implementation impl, String host, String database, String user, String passwd) {            
        super();                
		Driver d = load(impl.defaultDriverClassName());
		String url = impl.createJdbcUrl(host, database);		
		Properties drvcfg = new Properties();
		drvcfg.setProperty("user", user);		
		drvcfg.setProperty("password", passwd);
        this.implementation = impl;            
        this.driver = d;
        this.jdbcURL = url;
        this.driverConfig = drvcfg;

    }
    
    public Catalog getCatalog() throws SQLException, QueryException {
    	if (catalog == null) {
    		Connection c = newConnection();
			catalog = getImplementation().catalogFactory().create(c);
			c.close();
			
		}

		return catalog;
    }
    
    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
    
    public Implementation getImplementation() {
        return implementation;
    }
    public void setImplementation(Implementation environment) {
        this.implementation = environment;
    }
    
    public Connection connect() 
        throws SQLException {
    	String jdbcURL = getJdbcURL();
    	logger().debug("connect to: jdbcURL=" + jdbcURL);
    	logger().debug("with params: " + getDriverConfig());
        return driver.connect(jdbcURL, getDriverConfig());
    }
    
//        public void setConnection(Connection connection) {
//            this.connection = connection;
//        }
    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getJdbcURL() {
        return jdbcURL;
    }

    public void setJdbcURL(String jdbcURL) {
        this.jdbcURL = jdbcURL;
    }

    public Properties getDriverConfig() {
        return driverConfig;
    }
    
    @Override
    public String toString() {
        return TestSuiteBuilder.driverInfo(getDriver());
    }
    
    private static Logger logger() {
		return SimpleTestContext.logger;
	}
    
    public Driver load(String driverName) {
    	try {
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
	    	
	    	return selected;
    	}
    	catch (ClassNotFoundException e) {
    		logger().error(e.getMessage(), e);
    		throw new RuntimeException(e);
		}
    }
    
	@Override
	public Connection newConnection() throws SQLException {		
		Driver d = this.driver;		
		Properties cfg = getDriverConfig();
		Connection c = d.connect(getJdbcURL(), cfg);				

		return c;
	}
}