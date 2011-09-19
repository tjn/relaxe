/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.query.Query;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.query.QueryResult;
import fi.tnie.db.query.QueryTime;

public class QueryTask
{		
	private static Logger logger = Logger.getLogger(QueryTask.class);	
	private Query query;
	private ValueExtractorFactory valueExtractorFactory;
									
	public QueryTask(Query q, ValueExtractorFactory valueExtractorFactory) {
		super();
		this.query = q;
		this.valueExtractorFactory = valueExtractorFactory;
	}

	public QueryResult<DataObject> exec(Connection c) 
		throws QueryException {
			
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<DataObject> content = new ArrayList<DataObject>();
		QueryResult<DataObject> qr = new QueryResult<DataObject>(this.query, content);
				
		try {			
			QueryExpression qe = this.query.getExpression();
			
			String qs = qe.generate();
			
			ps = c.prepareStatement(qs);                
			qe.traverse(null, new AssignmentVisitor(null, ps));
			
			DataObjectReader qp = new DataObjectReader(valueExtractorFactory, qe, content);			
			qp.prepare();
			
			long ordinal = 0;
												
			try {
			    logger().debug("query: " + qs);
			    
			    long start = System.currentTimeMillis();
			    
				rs = ps.executeQuery();				
				
				long end = System.currentTimeMillis();
				qr.setQueryTime(end - start);				
				start = end;
								
				qp.startQuery(rs.getMetaData());
							
				while(rs.next()) {
					qp.process(rs, ++ordinal);
				}
				
				logger().debug("rows: " + ordinal);
				
				qp.endQuery();
				
				end = System.currentTimeMillis();
				qr.setPopulationTime(end - start);
			}
			catch (SQLException e) {
				qp.abort(e);
				throw e;
			}
			finally {				
				qp.finish();								
			}		
		} 
		catch (Throwable e) {
			logger().error(e.getMessage(), e);
			throw new QueryException(e.getMessage(), e);
		}
		finally {			
			rs = QueryHelper.doClose(rs);
			ps = QueryHelper.doClose(ps);			
		}
		
		return qr;
	}

	private static Logger logger() {
		return QueryTask.logger;
	}

}
