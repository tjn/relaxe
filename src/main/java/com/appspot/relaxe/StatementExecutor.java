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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.exec.QueryProcessor;
import com.appspot.relaxe.exec.ResultSetProcessor;
import com.appspot.relaxe.exec.UpdateProcessor;
import com.appspot.relaxe.expr.SQLDataChangeStatement;
import com.appspot.relaxe.expr.SelectStatement;
import com.appspot.relaxe.expr.Statement;
import com.appspot.relaxe.expr.Statement.Name;
import com.appspot.relaxe.expr.ddl.SQLSchemaStatement;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.query.QueryTime;
import com.appspot.relaxe.rdbms.PersistenceContext;

public class StatementExecutor {

	private static Logger logger = LoggerFactory.getLogger(StatementExecutor.class);
			
	private ValueAssignerFactory valueAssignerFactory = null; 
	private ValueExtractorFactory valueExtractorFactory = null;
	
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
		
		executeSelect(statement, c, p);				
		return p.get();
	}
	
	public QueryTime executeSelect(Statement statement, Connection c, ResultSetProcessor rp)
			throws SQLException, QueryException {

	    QueryTime qt = null;
	    
		long start = System.currentTimeMillis();
		
		String qs = statement.generate();
		long elapsed = System.currentTimeMillis() - start;
		
		logger().info("statement.generate(): {} ms", elapsed);
		logger().info("statement: {}", qs);
		
		rp.prepare();
		
		PreparedStatement ps = null;
		
		ps = c.prepareStatement(qs, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		
		AssignmentVisitor av = new AssignmentVisitor(valueAssignerFactory, ps);
		statement.traverse(null, av);
											
		ResultSet rs = null;
		
		try {
			final long s = System.currentTimeMillis();
			
			rs = ps.executeQuery();						
			
			final long f = System.currentTimeMillis();
			
			logger().info("ps.executeQuery(): {}ms : {}", (f - s), ps.toString());
			
			apply(rp, rs);
			
			final long p = System.currentTimeMillis();
			qt = new QueryTime(f - s, p - f);
		}
		finally {
			doClose(rs);
		}
		
		return qt;		
	}	
	
	
	
	private QueryTime exec(Statement statement, Connection c, UpdateProcessor up)
			throws SQLException, QueryException {

	    QueryTime qt = null;
	    
		try {			
			if (up != null) {
				up.prepare();
			}
			
			long start = System.currentTimeMillis();
			
			String qs = statement.generate();
			long elapsed = System.currentTimeMillis() - start;
			
			logger().info("generate(): " + elapsed + "ms");
			
			logger().info("execute: qs=" + qs);
			
			PreparedStatement ps = null;
			Name name = statement.getName();
			
			if (name == Name.INSERT) {
				ps = c.prepareStatement(qs, java.sql.Statement.RETURN_GENERATED_KEYS);
			}
			else {
				ps = c.prepareStatement(qs);
			}
			
			AssignmentVisitor av = new AssignmentVisitor(valueAssignerFactory, ps);
			statement.traverse(null, av);
												
			    
		    // org.postgresql.util.PSQLException: Can't use query methods that take a query string on a PreparedStatement.			    
//					int updated = ps.executeUpdate(java.sql.Statement.RETURN_GENERATED_KEYS);
			
			final long s = System.currentTimeMillis();
			int updated = ps.executeUpdate();
			final long u = System.currentTimeMillis();
			
			logger().info("executeUpdate(): " + (u - s) + "ms");
			
			qt = new QueryTime(u - s);				
				
			// ResultSet rs = ps.getResultSet();			
			if (up != null) {				
				up.updated(updated);
			}
			
		}
		finally {
			if (up != null) {
				up.finish();
			}
		}
		
		return qt;		
	}
	
	
	
//	public QueryTime execute(Statement statement, Connection c, ResultSetProcessor rp)
//			throws SQLException, QueryException {
//		return execute(statement, c, rp, null);
//	}
	
	public QueryTime executeUpdate(SQLDataChangeStatement statement, Connection c)
			throws SQLException, QueryException {
		return exec(statement, c, null);
	}
	
	public QueryTime executeUpdate(SQLDataChangeStatement statement, Connection c, UpdateProcessor up)
			throws SQLException, QueryException {
		return exec(statement, c, up);
	}
	
	public QueryTime execute(SQLSchemaStatement statement, Connection c, UpdateProcessor up)
			throws SQLException, QueryException {
		return exec(statement, c, up);
	}
	
	public QueryTime execute(SQLSchemaStatement statement, Connection c)
			throws SQLException, QueryException {		
		return exec(statement, c, null);
	}
	
	public QueryTime execute(Statement statement, Connection c, QueryProcessor qp)
		throws SQLException, QueryException {
		
		QueryTime qt = null;
				
		Statement.Name n = statement.getName();			
		
		if (n == null) {
			qt = exec(statement, c, qp);
		}
		else {	
			switch (n) {
			case SELECT:			
				qt = executeSelect(statement, c, qp);
				break;
			case CALL:			
				qt = executeCall(statement, c, qp);
				break;			
			default:
				qt = exec(statement, c, qp);			
				break;
			}
		}

		return qt;
	}

	private QueryTime executeCall(Statement statement, Connection c, QueryProcessor qp) 
		throws SQLException, QueryException {
	    
		QueryTime qt = null;
	    	    
		try {			
			qp.prepare();			
					
			String qs = statement.generate();
			
			PreparedStatement ps = c.prepareCall(qs);
						
			AssignmentVisitor av = new AssignmentVisitor(valueAssignerFactory, ps);
			statement.traverse(null, av);
			
			int uc = -1;
			
//			long start = System.currentTimeMillis();			
			
			boolean moreResults = ps.execute();
			
//			long elapsed = System.currentTimeMillis() - start;
			
			ResultSet rs = null;
			
			do {
				if (moreResults) {
					try {
						rs = ps.getResultSet();				
						apply(qp, rs);
					}
					finally {
						doClose(rs);
					}
				}
				else {
					uc = ps.getUpdateCount();
					
					if (uc != -1) {
						qp.updated(uc);
					}
				}
				
				moreResults = ps.getMoreResults(java.sql.Statement.CLOSE_CURRENT_RESULT);				
			}
			while (moreResults || (uc != -1));			
		}
		finally {
			qp.finish();		
		}
		
		return qt;
	}

	public void apply(ResultSetProcessor qp, ResultSet rs) 
			throws QueryException, SQLException {
		
		try {		
			qp.startResultSet(rs.getMetaData());
			
			long ordinal = 1;
			
			while(rs.next()) {
				qp.process(rs, ordinal++);
			}
			
			qp.endResultSet();
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
