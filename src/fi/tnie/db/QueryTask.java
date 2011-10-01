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
import fi.tnie.db.expr.AbstractQueryExpression;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.SelectStatement;
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
		QueryTime qt = null;				
		
		try {			
			QueryExpression qe = this.query.getExpression();
			SelectStatement ss = new SelectStatement(qe);			
			
			String qs = qe.generate();
			
			ps = c.prepareStatement(qs);                
			qe.traverse(null, new AssignmentVisitor(null, ps));
			
			DataObjectReader qp = new DataObjectReader(valueExtractorFactory, ss, content);			
			qp.prepare();
			
			long ordinal = 0;
												
			try {
			    logger().debug("query: " + qs);
			    
			    final long s = System.currentTimeMillis();			    
				rs = ps.executeQuery();							
				final long e = System.currentTimeMillis();
								
				qp.startQuery(rs.getMetaData());
							
				while(rs.next()) {
					qp.process(rs, ++ordinal);
				}
				
				logger().debug("rows: " + ordinal);
				
				qp.endQuery();
				
				final long p = System.currentTimeMillis();
				qt = new QueryTime(e - s, p - e);
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
				
		return new QueryResult<DataObject>(this.query, content, qt);
	}

	private static Logger logger() {
		return QueryTask.logger;
	}

}
