/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env.gae;

import fi.tnie.db.env.mysql.MySQLImplementation;

/**
 * @author Administrator
 *
 */
public class AppEngineImplementation
	extends MySQLImplementation {
    
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