/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import fi.tnie.db.exec.QueryFilter;
import fi.tnie.db.exec.QueryOffsetProcessor;
import fi.tnie.db.exec.QueryProcessor;
import fi.tnie.db.exec.QueryProcessorAdapter;
import fi.tnie.db.exec.QueryWindowProcessor;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.expr.DefaultTableExpression;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.SelectListElement;
import fi.tnie.db.expr.ValueElement;
import fi.tnie.db.expr.TableColumnExpr;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.DataType;
import fi.tnie.db.meta.ForeignKey;

public class DefaultEntityQuery<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, Q, ? extends E>
	> implements EntityQuery<A, R, Q, E>
{		
	private EntityMetaData<A, R, Q, ? extends E> meta;
	private EntityFactory<A, R, Q, ? extends E> factory;
	private DefaultTableExpression query;
	
	private TableReference tableRef;
			
	private static Logger logger = Logger.getLogger(DefaultEntityQuery.class);
									
	public DefaultEntityQuery(EntityMetaData<A, R, Q, ? extends E> meta) {
		super();				
		this.meta = meta;
		this.factory = meta.getFactory();
	}	
	
	public DefaultTableExpression getQuery() {		
		if (query == null) {
			DefaultTableExpression q = new DefaultTableExpression();		
			TableReference tref = getTableRef();
			q.setFrom(new From(tref));
					
			Select s = new Select();
			
			ElementList<SelectListElement> p = s.getSelectList();

			for (A a : meta.attributes()) {
				Column c = meta.getColumn(a);
				TableColumnExpr tce = new TableColumnExpr(tref, c);
				p.add(new ValueElement(tce, tce.getColumnName()));
			}
			
			// get columns from foreign keys, 
			// but remove duplicates
			
			Set<Column> kc = new HashSet<Column>();
			
			for (R r : meta.relationships()) {								
				ForeignKey fk = meta.getForeignKey(r);				
				kc.addAll(fk.columns().keySet());				
			}
			
			for (Column c : kc) {
				p.add(new ValueElement(new TableColumnExpr(tref, c)));	
			}
			
			logger().debug("projection: " + p);			
						
			q.setSelect(s);
			this.query = q;
		}
		
		return this.query;		
	}	
	
	/* (non-Javadoc)
	 * @see fi.tnie.db.EntityQuery#exec(java.sql.Connection)
	 */
	public EntityQueryResult<A, R, Q, E> exec(Connection c) 
		throws EntityQueryException {
		return exec(null, c);
	}
	
	/* (non-Javadoc)
	 * @see fi.tnie.db.EntityQuery#exec(long, java.lang.Long, java.sql.Connection)
	 */
	public EntityQueryResult<A, R, Q, E> exec(long offset, Long limit, Connection c) 
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
	public EntityQueryResult<A, R, Q, E> exec(QueryFilter qf, Connection c) 
		throws EntityQueryException {
			
		PreparedStatement ps = null;
		ResultSet rs = null;
		EntityQueryResult<A, R, Q, E> qr = null;
				
		try {
			DefaultTableExpression qo = getQuery();
			
			String qs = qo.generate();
			
			ps = c.prepareStatement(qs);                
			qo.traverse(null, new ParameterAssignment(ps));     
						
			final EntityQueryProcessor ep = new EntityQueryProcessor(this, qo);
			
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

	public void setFactory(EntityFactory<A, R, Q, E> factory) {
		this.factory = factory;
	}

	public EntityFactory<A, R, Q, ? extends E> getFactory() {
		return factory;
	}
	
	public class AttributeExtractor {		
		private A attribute;		
		private ValueExtractor extractor;
		
		public AttributeExtractor(A attribute, ValueExtractor extractor) {
			super();
			this.attribute = attribute;
			this.extractor = extractor;
		}

		public void set(E dest) {
			dest.set(this.attribute, extractor.last());			
		}		
	}
	
		
	private static class IntExtractor
		extends ValueExtractor
	{
		public IntExtractor(int column) {
			super(column);			
		}

		@Override
		public Integer doExtract(ResultSet rs) throws SQLException {
			int v = rs.getInt(getColumn());
			return rs.wasNull() ? null : new Integer(v);			
		}
	}
	
	private static class StringExtractor
		extends ValueExtractor
	{
		public StringExtractor(int column) {
			super(column);			
		}
		
		@Override
		public String doExtract(ResultSet rs) throws SQLException {
			return rs.getString(getColumn());			
		}
	}
	
	private static class ObjectExtractor
		extends ValueExtractor
	{
		public ObjectExtractor(int column) {
			super(column);			
		}
		
		@Override
		public Object doExtract(ResultSet rs) throws SQLException {
			return rs.getObject(getColumn());
		}
	}
	
	public class EntityQueryProcessor 
		extends QueryProcessorAdapter {
		
		private Extractor[] extractors = null;
		private List<AttributeExtractor> attributeWriterList;
		private int attrs;
		private List<E> content;
		private E first;
		private EntityQuery<A, R, Q, E> source;
		private boolean completed;
								
		public EntityQueryProcessor(EntityQuery<A, R, Q, E> source, DefaultTableExpression qo) {
			int colno = 0;			
			this.source = source;
			this.completed = false;
									
			BaseTable table = meta.getBaseTable();
			
			List<? extends ColumnName> cl = qo.getSelect().getColumnNameList().getContent();
			
			Extractor[] xa = new Extractor[cl.size()];
			List<AttributeExtractor> awl = new ArrayList<AttributeExtractor>();
									
			for (ColumnName n : qo.getSelect().getColumnNameList().getContent()) {
				colno++;
				
				Column column = table.getColumn(n);
				logger().debug(n + " => " + column);
				
				DataType t = column.getDataType();
				int sqltype = t.getDataType();
				
				ValueExtractor e = null;
					
				switch (sqltype) {
					case Types.INTEGER:					
					case Types.SMALLINT:
					case Types.TINYINT:
						e = new IntExtractor(colno);	
						break;
					case Types.VARCHAR:
					case Types.CHAR:
						e = new StringExtractor(colno);	
						break;					
					default:
						e = new ObjectExtractor(colno);
						break;
				}
				
				xa[colno - 1] = e;
				
				A a = meta.getAttribute(column);
				
				if (a != null) {
					awl.add(new AttributeExtractor(a, e));		
				}
			}			
			
			this.extractors = xa;
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

		private EntityQueryResult<A, R, Q, E> getQueryResult(
				EntityQuery<A, R, Q, E> source, long available) {
			
			if (!this.completed) {
				return null;
			}

			if (this.first == null) {
				return new EmptyEntityQueryResult<A, R, Q, E>(this.source, available);
			}
			
			if (this.content == null) {
				return new SingleEntityQueryResult<A, R, Q, E>(this.source, this.first, available);
			}
			
			return new MultipleEntityQueryResult<A, R, Q, E>(this.source, this.content, available);
		}

		@Override
		public void process(ResultSet rs, long ordinal) throws QueryException {
			try {
				EntityFactory<A, R, Q, ? extends E> ef = getFactory();				
				E e = ef.newInstance();
				
				for (int i = 0; i < this.extractors.length; i++) {
					this.extractors[i].extract(rs);				
				}
				
				for (int i = 0; i < attrs; i++) {
					AttributeExtractor ax = this.attributeWriterList.get(i);
					ax.set(e);
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
		return DefaultEntityQuery.logger;
	}

    public TableReference getTableRef() {
        if (this.tableRef == null) {
            this.tableRef = new TableReference(meta.getBaseTable());
        }
        
        return this.tableRef;
    }    
}
