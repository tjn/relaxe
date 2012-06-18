/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.apache.log4j.Logger;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.exec.QueryProcessor;
import fi.tnie.db.expr.SelectStatement;
import fi.tnie.db.expr.Statement;
import fi.tnie.db.expr.Statement.Name;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.query.QueryTime;

public class StatementExecutor {

	private static Logger logger = Logger.getLogger(StatementExecutor.class);
			
	private ValueAssignerFactory valueAssignerFactory = null; 
	private ValueExtractorFactory valueExtractorFactory = null;
		
	public StatementExecutor(Implementation implementation) {
		super();
		this.valueAssignerFactory = implementation.getValueAssignerFactory();
		this.valueExtractorFactory = implementation.getValueExtractorFactory();
	}

	public DataObject fetchFirst(SelectStatement statement, Connection c) throws SQLException, QueryException {
		MutableDataObjectProcessor p = new MutableDataObjectProcessor(valueExtractorFactory, statement) {
			@Override
			public void process(ResultSet rs, long ordinal) throws QueryException {			
				super.process(rs, ordinal);
			}
		};
		
		execute(statement, c, p);				
		return p.get();
	}
	
	public QueryTime execute(Statement statement, Connection c, QueryProcessor qp)
		throws SQLException, QueryException {

	    if (statement == null) {
            throw new NullPointerException("'statement' must not be null");
        }
	    
	    QueryTime qt = null;

		try {
			long start = System.currentTimeMillis();
			
			String qs = statement.generate();
			long elapsed = System.currentTimeMillis() - start;
			
			logger().info("generate(): " + elapsed + "ms");
			
			logger().info("execute: qs=" + qs);
			
			PreparedStatement ps = null;
			Name name = statement.getName();
			
			if (name == Name.CALL) {
				ps = c.prepareCall(qs);
			}
			else if (name == Name.INSERT) {
				ps = c.prepareStatement(qs, java.sql.Statement.RETURN_GENERATED_KEYS);
			}
			else if (name == Name.SELECT) {
				ps = c.prepareStatement(qs, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			}
			else {
				ps = c.prepareStatement(qs);
			}
			
			AssignmentVisitor av = new AssignmentVisitor(valueAssignerFactory, ps);
			statement.traverse(null, av);
												
			if (name == Name.SELECT) {
				ResultSet rs = null;
				
				try {
					qp.prepare();
					
					final long s = System.currentTimeMillis();										
					rs = ps.executeQuery();															
					
					final long f = System.currentTimeMillis();
					
					logger().info("executeQuery(): " + (f - s) + "ms");
					
					apply(qp, rs);
					
					final long p = System.currentTimeMillis();
					qt = new QueryTime(f - s, p - f);
				}
				catch (NullPointerException e) {
					logger.error(e.getMessage(), e);
					throw e;
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
				
				logger().info("executeUpdate(): " + (u - s) + "ms");
				
				qt = new QueryTime(u - s);				
				
				// ResultSet rs = ps.getResultSet();			
												
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

	public void apply(QueryProcessor qp, ResultSet rs) throws QueryException,
			SQLException {
		
		qp.startQuery(rs.getMetaData());
		
		long ordinal = 1;
		
		while(rs.next()) {
			qp.process(rs, ordinal++);
		}
		
		qp.endQuery();		
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
