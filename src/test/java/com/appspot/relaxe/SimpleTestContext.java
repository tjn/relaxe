/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.meta.Catalog;
import com.appspot.relaxe.query.QueryException;


/**
 * @author Administrator
 *
 */
public class SimpleTestContext<I extends Implementation<I>>
    implements EnvironmentTestContext, TestContext<I> {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleTestContext.class);

    private Catalog catalog;
    private I implementation;
    private Driver driver;
    private String jdbcURL;
    private Properties driverConfig;
            
    public SimpleTestContext(I impl, Driver driver, String jdbcURL, Properties driverConfig)
    	throws ClassNotFoundException {            
        super();
        this.implementation = impl;            
        this.driver = driver;
        this.jdbcURL = jdbcURL;
        this.driverConfig = driverConfig;
    }
    
    public SimpleTestContext(I impl) {
    	// TODO: remove user name
    	this(impl, null, "pagila", "pagila", "password");
    }
    
    
    public SimpleTestContext(I impl, String host, String database, String user, String passwd) {            
        super();                
		Driver d = load(impl.defaultDriverClassName());
		String url = impl.createJdbcUrl(host, database);		
				
		Properties drvcfg = impl.getDefaultProperties();
				
		drvcfg.setProperty("user", user);		
		drvcfg.setProperty("password", passwd);
		
        this.implementation = impl;            
        this.driver = d;
        this.jdbcURL = url;
        this.driverConfig = drvcfg;

    }
    
    @Override
	public Catalog getCatalog() throws SQLException, QueryException {
    	if (catalog == null) {
    		Connection c = newConnection();
			catalog = getImplementation().catalogFactory().create(c);
			c.close();
			
		}

		return catalog;
    }
    
    @Override
	public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
    
    @Override
	public I getImplementation() {
        return implementation;
    }
    public void setImplementation(I environment) {
        this.implementation = environment;
    }
    
    @Override
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
    @Override
	public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
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
		
		c.setAutoCommit(false);

		return c;
	}
	@Override
	public PersistenceContext<I> getPersistenceContext() {
		return null;
	}

	@Override
	public Properties getJdbcConfig() {
		return (Properties) this.driverConfig.clone();
	}
}