package com.appspot.relaxe.rdbms;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.rdbms.ConnectionManager;
import com.appspot.relaxe.rdbms.DefaultDataAccessContext;
import com.appspot.relaxe.rdbms.DefaultResolver;
import com.appspot.relaxe.rdbms.Implementation;
import com.appspot.relaxe.rdbms.PersistenceContext;
import com.appspot.relaxe.service.DataAccessContext;


public class DataAccessContextFactory {
	
	private static Logger logger = LoggerFactory.getLogger(DataAccessContextFactory.class);	
	
	public DataAccessContextFactory() {
	}
	
	
	public DataAccessContext newDataAccessContext(DataSource dataSource)
		throws SQLException {
		logger.debug("data-source: {}", dataSource);
				
		Connection c = dataSource.getConnection();
		String jdbcURL = null;
		
		try	{
			jdbcURL = c.getMetaData().getURL();	
		}
		finally {
			c.close();
		}
							
					
		ConnectionManager cm = new DataSourceConnectionManager(dataSource);
		
		DataAccessContext dac = newDataAccessContext(jdbcURL, cm);
		
		return dac;		
	}	
	
	public DataAccessContext newDataAccessContext(String jdbcURL, ConnectionManager cm) {
		DefaultResolver resolver = new DefaultResolver();
		PersistenceContext<?> pc = resolver.resolveDefaultContext(jdbcURL);	
		DefaultDataAccessContext<?> dac = createDataAccessContext(pc, cm);
		return dac;
	}
	
	private <
		I extends Implementation<I>
	>
	DefaultDataAccessContext<I> createDataAccessContext(PersistenceContext<I> pc, ConnectionManager cm) {
		DefaultDataAccessContext<I> dac = new DefaultDataAccessContext<I>(pc, cm);			
		return dac;
	}	
	
}
