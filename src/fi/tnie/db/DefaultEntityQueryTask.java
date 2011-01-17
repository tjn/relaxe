/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.VarcharType;
import fi.tnie.db.ent.EmptyEntityQueryResult;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.EntityQueryException;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.MultipleEntityQueryResult;
import fi.tnie.db.ent.SingleEntityQueryResult;
import fi.tnie.db.exec.QueryFilter;
import fi.tnie.db.exec.QueryOffsetProcessor;
import fi.tnie.db.exec.QueryProcessor;
import fi.tnie.db.exec.QueryProcessorAdapter;
import fi.tnie.db.exec.QueryWindowProcessor;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;

public class DefaultEntityQueryTask<
	A,
	R, 
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>> 
	implements EntityQueryTask<A, R, T, E>
{		
	
	private EntityQuery<A, R, T, E> entityQuery;
	private EntityFactory<A, R, T, E> factory;	
	
	private static Logger logger = Logger.getLogger(DefaultEntityQueryTask.class);
									
	public DefaultEntityQueryTask(EntityQuery<A, R, T, E> query) {
		super();				
		this.entityQuery = query;		
		this.factory = query.getMetaData().getFactory();
	}	
	
	
	/* (non-Javadoc)
	 * @see fi.tnie.db.EntityQuery#exec(java.sql.Connection)
	 */
	public EntityQueryResult<A, R, T, E> exec(Connection c) 
		throws EntityQueryException {
		return exec(null, c);
	}
	
	/* (non-Javadoc)
	 * @see fi.tnie.db.EntityQuery#exec(long, java.lang.Long, java.sql.Connection)
	 */
	public EntityQueryResult<A, R, T, E> exec(long offset, Long limit, Connection c) 
		throws EntityQueryException {
		QueryFilter qf = null;	
	
		if (limit != null) {
			qf = new QueryWindowProcessor(null, limit);
		}
					
		if (offset > 0) {
			qf = new QueryOffsetProcessor(qf, offset);
		}
	
		return exec(qf, c);
	}

	/* (non-Javadoc)
	 * @see fi.tnie.db.EntityQuery#exec(fi.tnie.db.exec.QueryFilter, java.sql.Connection)
	 */
	public EntityQueryResult<A, R, T, E> exec(QueryFilter qf, Connection c) 
		throws EntityQueryException {
			
		PreparedStatement ps = null;
		ResultSet rs = null;
		EntityQueryResult<A, R, T, E> qr = null;
				
		try {			
			DefaultTableExpression qo = this.entityQuery.getQuery();
			
			String qs = qo.generate();
			
			ps = c.prepareStatement(qs);                
			qo.traverse(null, new ParameterAssignment(ps));     
						
			final EntityQueryProcessor ep = new EntityQueryProcessor(entityQuery, qo);
			
			if (qf != null) {
				qf.setInner(ep);				
			}
			
			final QueryProcessor qp = (qf == null) ? ep : qf;
						
			qp.prepare();
			
			long ordinal = 0;
												
			try {
			    logger().debug("query: " + qs);
			    
				rs = ps.executeQuery();	
				qp.startQuery(rs.getMetaData());
							
				while(rs.next()) {
					qp.process(rs, ++ordinal);
				}
				
				logger().debug("rows: " + ordinal);
				
				qp.endQuery();
			}
			catch (SQLException e) {
				qp.abort(e);
				throw e;
			}
			finally {				
				qp.finish();								
			}
			
			qr = ep.getQueryResult(this, ordinal);
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

	public void setFactory(EntityFactory<A, R, T, E> factory) {
		this.factory = factory;
	}

	public EntityFactory<A, R, T, E> getFactory() {
		return factory;
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
	
	public class EntityQueryProcessor 
		extends QueryProcessorAdapter {
		
//		private Extractor[] extractors = null;
		private List<AttributeExtractor<A, R, T, E>> attributeWriterList;
		private int attrs;
		private List<E> content;
		private E first;
		private EntityQuery<A, R, T, E> source;
		private boolean completed;
								
		public EntityQueryProcessor(EntityQuery<A, R, T, E> source, DefaultTableExpression qo) {
			int colno = 0;			
			this.source = source;
			this.completed = false;
												
			EntityMetaData<A, R, T, ?> meta = source.getMetaData();
			BaseTable table = meta.getBaseTable();
			
			List<? extends ColumnName> cl = qo.getSelect().getColumnNameList().getContent();
			
			Extractor[] xa = new Extractor[cl.size()];
			List<AttributeExtractor<A, R, T, E>> awl = new ArrayList<AttributeExtractor<A, R, T, E>>();
									
			for (ColumnName n : qo.getSelect().getColumnNameList().getContent()) {
				colno++;
				
				Column column = table.getColumn(n);
				logger().debug(n + " => " + column);
				
				DataType t = column.getDataType();
				int sqltype = t.getDataType();
				
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
				
				A a = meta.getAttribute(column);
				
				if (a != null) {
					awl.add(new AttributeExtractor<A, R, T, E>(a, e));		
				}
			}			
			
//			this.extractors = xa;
			this.attributeWriterList = awl;
			this.attrs = awl.size();			
		}
		
		@Override
		public void startQuery(ResultSetMetaData m) throws QueryException,
				SQLException {			
			this.first = null;
			this.content = null;
			this.completed = false;
		}
		
		@Override
		public void endQuery() throws QueryException {
			this.completed = true;
		}

		private EntityQueryResult<A, R, T, E> getQueryResult(
				EntityQueryTask<A, R, T, E> source, long available) {
			
			if (!this.completed) {
				return null;
			}

			if (this.first == null) {
				return new EmptyEntityQueryResult<A, R, T, E>(this.source, available);
			}
			
			if (this.content == null) {
				return new SingleEntityQueryResult<A, R, T, E>(this.source, this.first, available);
			}
			
			return new MultipleEntityQueryResult<A, R, T, E>(this.source, this.content, available);
		}

		@Override
		public void process(ResultSet rs, long ordinal) throws QueryException {
			try {
				EntityFactory<A, R, T, E> ef = getFactory();				
				E e = ef.newInstance();
				
//				for (int i = 0; i < this.extractors.length; i++) {
//					this.extractors[i].extract(rs);				
//				}
				
//				for (int i = 0; i < attrs; i++) {
//					AttributeExtractor ax = this.attributeWriterList.get(i);
//					ax.set(e);
//				}
				
				for (int i = 0; i < attrs; i++) {
					AttributeExtractor<A, R, T, E> ax = this.attributeWriterList.get(i);
					ax.extract(rs, e);
				}
								
				if (this.first == null) {
					this.first = e;
				}
				else {
					if (this.content == null) {
						this.content = new ArrayList<E>();
						this.content.add(this.first);
					}
					
					this.content.add(e);
				}
			}
			catch (Throwable e) {
				throw new QueryException(e.getMessage(), e);
			}
		}
	}
	
	private static Logger logger() {
		return DefaultEntityQueryTask.logger;
	}

}
