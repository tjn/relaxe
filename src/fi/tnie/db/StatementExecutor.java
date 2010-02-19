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
	
	private Statement statement; 
	
	private StatementExecutor(Statement statement) {
		super();
		
		if (statement == null) {
			throw new NullPointerException("'statement' must not be null");
		}
		
		this.statement = statement;
	}

	public void execute(Connection c, QueryProcessor qp)
		throws SQLException {

		try {
			Statement s = this.statement;
			String qs = s.generate();
			PreparedStatement ps = null;
			
			if (s.getName() == Name.CALL) {
				ps = c.prepareCall(qs);
			}
			else {
				ps = c.prepareStatement(qs, java.sql.Statement.RETURN_GENERATED_KEYS);
			}
									
			if (s.getName().equals(Name.SELECT)) {
				ResultSet rs = null;
				
				try {
					rs = ps.executeQuery();					
				}
				finally {
					doClose(rs);
				}								
			}
			else {
				int updated = ps.executeUpdate(qs, java.sql.Statement.RETURN_GENERATED_KEYS);
				
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
