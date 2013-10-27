/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import org.apache.log4j.Logger;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.exec.QueryProcessor;
import com.appspot.relaxe.exec.QueryProcessorAdapter;
import com.appspot.relaxe.exec.ResultProcessor;
import com.appspot.relaxe.exec.ResultSetProcessor;
import com.appspot.relaxe.exec.UpdateProcessor;
import com.appspot.relaxe.expr.SQLDataChangeStatement;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.Statement.Name;
import com.appspot.relaxe.expr.ddl.SQLSchemaStatement;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.query.QueryTime;


public class StatementExecutor {

	private static Logger logger = Logger.getLogger(StatementExecutor.class);
			
	private ValueAssignerFactory valueAssignerFactory = null; 
	private ValueExtractorFactory valueExtractorFactory = null;
	
	
	private static final UpdateProcessor NO_OPERATION = new QueryProcessorAdapter();
		
	public StatementExecutor(PersistenceContext<?> persistenceContext) {
		super();
		this.valueAssignerFactory = persistenceContext.getValueAssignerFactory();
		this.valueExtractorFactory = persistenceContext.getValueExtractorFactory();
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
	
	private QueryTime execute(Statement statement, Connection c, ResultSetProcessor rp, UpdateProcessor up)
			throws SQLException, QueryException {

	    QueryTime qt = null;
	    
	    ResultProcessor qp = (rp == null) ? up : rp;

		try {			
			qp.prepare();
			
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
												
			if (rp != null) {
				ResultSet rs = null;
				
				try {
					final long s = System.currentTimeMillis();										
					rs = ps.executeQuery();															
					
					final long f = System.currentTimeMillis();
					
					logger().info("executeQuery(): " + (f - s) + "ms");
					
					apply(rp, rs);
					
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
//					int updated = ps.executeUpdate(java.sql.Statement.RETURN_GENERATED_KEYS);
				
				final long s = System.currentTimeMillis();
				int updated = ps.executeUpdate();
				final long u = System.currentTimeMillis();
				
				logger().info("executeUpdate(): " + (u - s) + "ms");
				
				qt = new QueryTime(u - s);				
				
				// ResultSet rs = ps.getResultSet();			
												
				up.updated(updated);
				
				if (updated > 0) {
					
				}
			}
		}
		finally {
			qp.finish();		
		}
		
		return qt;		
	}
	
	
	
	public QueryTime execute(Statement statement, Connection c, ResultSetProcessor rp)
			throws SQLException, QueryException {
		return execute(statement, c, rp, null);
	}
	
	public QueryTime execute(SQLDataChangeStatement statement, Connection c)
			throws SQLException, QueryException {
		return execute(statement, c, null, NO_OPERATION);
	}
	
	public QueryTime execute(SQLDataChangeStatement statement, Connection c, UpdateProcessor up)
			throws SQLException, QueryException {
		return execute(statement, c, null, up);
	}
	
	public QueryTime execute(SQLSchemaStatement statement, Connection c, UpdateProcessor up)
			throws SQLException, QueryException {
		return execute(statement, c, null, up);
	}
	
	public QueryTime execute(SQLSchemaStatement statement, Connection c)
			throws SQLException, QueryException {		
		return execute(statement, c, NO_OPERATION);
	}
	
	public QueryTime execute(Statement statement, Connection c, QueryProcessor qp)
		throws SQLException, QueryException {
		
		boolean read = (statement.getName() == Name.SELECT);
		ResultSetProcessor rp = read ? qp : null;
		UpdateProcessor up = read ? null : qp;
		return execute(statement, c, rp, up);

//	    if (statement == null) {
//            throw new NullPointerException("'statement' must not be null");
//        }
//	    
//	    QueryTime qt = null;
//
//		try {
//			qp.prepare();
//			
//			long start = System.currentTimeMillis();
//			
//			String qs = statement.generate();
//			long elapsed = System.currentTimeMillis() - start;
//			
//			logger().info("generate(): " + elapsed + "ms");			
//			logger().info("execute: qs=" + qs);
//			
//			PreparedStatement ps = null;
//			Name name = statement.getName();
//			
//			if (name == Name.CALL) {
//				ps = c.prepareCall(qs);
//			}
//			else if (name == Name.INSERT) {
//				ps = c.prepareStatement(qs, java.sql.Statement.RETURN_GENERATED_KEYS);
//			}
//			else if (name == Name.SELECT) {
//				ps = c.prepareStatement(qs, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
//			}
//			else {
//				ps = c.prepareStatement(qs);
//			}
//			
//			AssignmentVisitor av = new AssignmentVisitor(valueAssignerFactory, ps);
//			statement.traverse(null, av);
//												
//			if (name == Name.SELECT) {
//				ResultSet rs = null;
//				
//				try {
//					final long s = System.currentTimeMillis();										
//					rs = ps.executeQuery();															
//					
//					final long f = System.currentTimeMillis();
//					
//					logger().info("executeQuery(): " + (f - s) + "ms");
//					
//					apply(qp, rs);
//					
//					final long p = System.currentTimeMillis();
//					qt = new QueryTime(f - s, p - f);
//				}
//				catch (NullPointerException e) {
//					logger.error(e.getMessage(), e);
//					throw e;
//				}
//				finally {
//					doClose(rs);
//				}
//			}
//			else {
//			    
//			    // org.postgresql.util.PSQLException: Can't use query methods that take a query string on a PreparedStatement.			    
////				int updated = ps.executeUpdate(java.sql.Statement.RETURN_GENERATED_KEYS);
//				
//				final long s = System.currentTimeMillis();
//				int updated = ps.executeUpdate();
//				final long u = System.currentTimeMillis();
//				
//				logger().info("executeUpdate(): " + (u - s) + "ms");
//				
//				qt = new QueryTime(u - s);				
//				
//				// ResultSet rs = ps.getResultSet();			
//												
//				qp.updated(updated);
//				
//				if (updated > 0) {
//					
//				}
//			}
//		}
//		finally {
//			qp.finish();		
//		}
//		
//		return qt;		
	}

	public void apply(ResultSetProcessor qp, ResultSet rs) 
			throws QueryException, SQLException {
		
		try {		
			qp.startQuery(rs.getMetaData());
			
			long ordinal = 1;
			
			while(rs.next()) {
				qp.process(rs, ordinal++);
			}
			
			qp.endQuery();
		}
		catch (Exception e) {
			qp.abort(e);
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
