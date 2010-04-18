/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Environment;

/**
     * @author Administrator
     *
     */
    class SimpleTestContext
        implements EnvironmentTestContext {

        private Catalog catalog;
        private Environment environment;
        private Driver driver;
        private String jdbcURL;
        private Properties driverConfig;
                
        protected SimpleTestContext(Environment environment, Driver driver, String jdbcURL, Properties driverConfig) {            
            super();
            this.environment = environment;            
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
        
        public Environment getEnvironment() {
            return environment;
        }
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }
        
        public Connection connect() 
            throws SQLException {
            return driver.connect(getJdbcURL(), getDriverConfig());
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
    }