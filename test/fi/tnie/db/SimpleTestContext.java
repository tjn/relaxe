/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import fi.tnie.db.env.Implementation;
import fi.tnie.db.meta.Catalog;

/**
 * @author Administrator
 *
 */
public class SimpleTestContext
    implements EnvironmentTestContext {
	
	private static Logger logger = Logger.getLogger(SimpleTestContext.class);

    private Catalog catalog;
    private Implementation implementation;
    private Driver driver;
    private String jdbcURL;
    private Properties driverConfig;
            
    protected SimpleTestContext(Implementation impl, Driver driver, String jdbcURL, Properties driverConfig) {            
        super();
        this.implementation = impl;            
        this.driver = driver;
        this.jdbcURL = jdbcURL;
        this.driverConfig = driverConfig;
    }
    
    public Catalog getCatalog() {
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
}