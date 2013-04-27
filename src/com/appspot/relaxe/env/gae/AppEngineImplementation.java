/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.env.gae;

import com.appspot.relaxe.env.mysql.MySQLImplementation;

/**
 * @author Administrator
 *
 */
public class AppEngineImplementation
	extends MySQLImplementation {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2714061809484656075L;

	@Override
    public String defaultDriverClassName() {
        return "com.google.appengine.api.rdbms.AppEngineDriver";
    }
    
    @Override
	public String createJdbcUrl(String database) {
    	return createJdbcUrl(null, database);
    }

    /**
     * instanceName
     */
	@Override
	public String createJdbcUrl(String instanceName, String database) {
		if (instanceName == null) {
			throw new NullPointerException("instanceName");
		}
				
		return "jdbc:google:rdbms://" + instanceName + "/" + database;
	}

	/**
	 * Equivalent to <code>createJdbcUrl(host, database)</code>
	 * 
	 * <code>port</code> is ignored.
	 */
	@Override
	public String createJdbcUrl(String instanceName, int port, String database) {
		return createJdbcUrl(instanceName, database);
	}	
}
