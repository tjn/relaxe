/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.VarcharType;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.EntityQueryException;
import fi.tnie.db.exec.QueryProcessorAdapter;
import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.expr.TableExpression;
import fi.tnie.db.expr.ValueExpression;

public class QueryTask
{		
	private static Logger logger = Logger.getLogger(QueryTask.class);	
	private Query query;
									
	public QueryTask(Query q) {
		super();				
		this.query = q;
	}

	/* (non-Javadoc)
	 * @see fi.tnie.db.EntityQuery#exec(fi.tnie.db.exec.QueryFilter, java.sql.Connection)
	 */
	public QueryResult<DataObject> exec(Connection c) 
		throws EntityQueryException {
			
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<DataObject> content = new ArrayList<DataObject>();
		QueryResult<DataObject> qr = new QueryResult<DataObject>(this.query, content);
				
		try {			
			QueryExpression qe = this.query.getExpression();
			
			String qs = qe.generate();
			
			ps = c.prepareStatement(qs);                
			qe.traverse(null, new ParameterAssignment(ps));
			
			DataObjectReader qp = new DataObjectReader(qe, content);			
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
			throw new EntityQueryException(e.getMessage(), e);
		}
		finally {			
			rs = QueryHelper.doClose(rs);
			ps = QueryHelper.doClose(ps);			
		}
		
		return qr;
	}

	private static class ObjectExtractor
		extends ValueExtractor<Serializable, VarcharType>
	{
		public ObjectExtractor(int column) {
			super(column);			
		}
		
		@Override
		public PrimitiveHolder<Serializable, VarcharType> extractValue(ResultSet rs) throws SQLException {
//			return rs.getObject(getColumn());
			throw new UnsupportedOperationException("unsupported: " + getClass() + ".extractValue()");
		}
	}
	
	public class DataObjectReader
		extends QueryProcessorAdapter {
		
		private List<DataObject> content;		
		private DefaultDataObject.MetaData meta;
		private ValueExtractor<?, ?>[] extractors;
										
		public DataObjectReader(QueryExpression qo, List<DataObject> content) {									
			TableExpression te = qo.getTableExpr();
			List<ValueExpression> el = te.getSelect().expandValueExprList();
			
			this.content = content;
			this.meta = new DefaultDataObject.MetaData(qo);			
			this.extractors = createExtractorArray(el);
		}

		private ValueExtractor<?, ?>[] createExtractorArray(List<ValueExpression> el) {
			int colno = 0;
			ValueExtractor<?, ?>[] xa = new ValueExtractor<?, ?>[el.size()];
																		
			for (ValueExpression expr : el) {
				colno++;
				
				int sqltype = expr.getType();		
				
				ValueExtractor<?, ?> e = null;
					
				switch (sqltype) {
					case Types.INTEGER:					
					case Types.SMALLINT:
					case Types.TINYINT:
						e = new IntExtractor(colno);	
						break;
					case Types.VARCHAR:
					case Types.CHAR:
						e = new VarcharExtractor(colno);	
						break;					
					default:
						e = new ObjectExtractor(colno);
						break;
				}
				
				xa[colno - 1] = e;
			}
			
			return xa;
		}
				
		@Override
		public void process(ResultSet rs, long ordinal) throws QueryException {
			try {
				DefaultDataObject o = new DefaultDataObject(this.meta);
				
				int count = this.extractors.length;
				
				for (int i = 0; i < count; i++) {
					ValueExtractor<?, ?> ve = this.extractors[i];
					PrimitiveHolder<?, ?> h = ve.extractValue(rs);
					o.set(i, h);
				}			
				
				this.content.add(o);																
			}
			catch (Throwable e) {
				throw new QueryException(e.getMessage(), e);
			}
		}
	}
	
	private static Logger logger() {
		return QueryTask.logger;
	}

}
