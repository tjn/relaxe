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

public class StatementExecutor {

	private static Logger logger = Logger.getLogger(StatementExecutor.class);
	
	public void execute(Statement statement, Connection c, QueryProcessor qp)
		throws SQLException, QueryException {

	    if (statement == null) {
            throw new NullPointerException("'statement' must not be null");
        }

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
					
					rs = ps.executeQuery();
					
					qp.startQuery(rs.getMetaData());
					
					long ordinal = 1;
					
					while(rs.next()) {
						qp.process(rs, ordinal++);
					}
					
					qp.endQuery();
				}
				finally {
					doClose(rs);
				}								
			}
			else {
			    
			    // org.postgresql.util.PSQLException: Can't use query methods that take a query string on a PreparedStatement.			    
//				int updated = ps.executeUpdate(java.sql.Statement.RETURN_GENERATED_KEYS);
				int updated = ps.executeUpdate();
				
				qp.updated(updated);
				
				if (updated > 0) {					
					
				}
			}
		}
		finally {
			qp.finish();		
		}		
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
