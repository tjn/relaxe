/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe.env.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.feature.SQLGenerationException;
import com.appspot.relaxe.query.QueryException;


public abstract class Tool {

	private static Logger logger = LoggerFactory.getLogger(Tool.class);
		
//	private void run(Implementation<?> env, String jdbcUrl, Properties jdbcConfig) 
//		throws Exception {
//	        		
//	    String driverName = env.defaultDriverClassName();
//	    logger().debug("loading " + driverName);
//		Class<?> driverClass = Class.forName(driverName);
//		logger().debug("driver loaded.");
//		
//		Driver selected = null;
//		
//		List<Driver> loaded = Collections.list(DriverManager.getDrivers());
//		
//		for(Driver d : loaded) {
//			if (d.getClass().equals(driverClass)) {
//				selected = d;
//				break;
//			}				
//		}
//		
//		if (!selected.acceptsURL(jdbcUrl)) {
//			throw new IllegalArgumentException(
//					"Driver " + selected.getClass() + " does not accept URL: " + jdbcUrl);
//		}
//		
//		logger().debug("connecting to: " + jdbcUrl);
//		
//		Connection c = selected.connect(jdbcUrl, jdbcConfig);
//				
//		logger().debug("connected.");
//		
//		if (c == null) {
//			throw new IllegalArgumentException("can not create connection to " + jdbcUrl);
//		}
//		
//		try {
//			run(env.self(), c);
//		}
//		finally {
//		    close(c);		    
//		}		
//	}

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

    public abstract 
    <I extends Implementation<I>>    
    void run(Implementation<I> env, Connection c)
		throws QueryException, IOException, SQLGenerationException, SQLException;

	public static Logger logger() {
		return Tool.logger;
	}
}
