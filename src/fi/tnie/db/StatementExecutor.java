/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.apache.log4j.Logger;

import fi.tnie.db.exec.QueryProcessor;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.Statement.Name;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.query.QueryTime;

public class StatementExecutor {

	private static Logger logger = Logger.getLogger(StatementExecutor.class);
	
	public QueryTime execute(Statement statement, Connection c, QueryProcessor qp)
		throws SQLException, QueryException {

	    if (statement == null) {
            throw new NullPointerException("'statement' must not be null");
        }
	    
	    QueryTime qt = null;

		try {
			String qs = statement.generate();
			PreparedStatement ps = null;
			
			if (statement.getName() == Name.CALL) {
				ps = c.prepareCall(qs);
			}
			else {
			    if (statement.getName() == Name.INSERT) {
			        ps = c.prepareStatement(qs, java.sql.Statement.RETURN_GENERATED_KEYS);
			    }
			    else {
			        ps = c.prepareStatement(qs);
			    }
			}
									
			if (statement.getName().equals(Name.SELECT)) {
				ResultSet rs = null;
				
				try {
					qp.prepare();
					
					final long s = System.currentTimeMillis();										
					rs = ps.executeQuery();
					final long f = System.currentTimeMillis();
					
					qp.startQuery(rs.getMetaData());
					
					long ordinal = 1;
					
					while(rs.next()) {
						qp.process(rs, ordinal++);
					}
					
					qp.endQuery();
					
					final long p = System.currentTimeMillis();					
					qt = new QueryTime(f - s, p - f);
				}
				finally {
					doClose(rs);
				}								
			}
			else {
			    
			    // org.postgresql.util.PSQLException: Can't use query methods that take a query string on a PreparedStatement.			    
//				int updated = ps.executeUpdate(java.sql.Statement.RETURN_GENERATED_KEYS);
				
				final long s = System.currentTimeMillis();
				int updated = ps.executeUpdate();
				final long u = System.currentTimeMillis();				
				qt = new QueryTime(u - s);
				
				qp.updated(updated);
				
				if (updated > 0) {					
					
				}
			}
		}
		finally {
			qp.finish();		
		}
		
		return qt;		
	}

	
	private void doClose(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException e) {
				logger().warn(e.getMessage());
			}
		}
	}
	
	public static Logger logger() {
		return StatementExecutor.logger;
	}
	
}
