/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import fi.tnie.db.meta.ForeignKey;

public class DefaultEntityQuery<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends Entity<A, R, E>
	>
{		
//	private EnumMap<A, Integer> projection;
	private EntityMetaData<A, R, Q, E> meta;
	private EntityFactory<A, R, Q, E> factory;
	private DefaultTableExpression query;
									
	public DefaultEntityQuery(EntityMetaData<A, R, Q, E> meta) {
		super();				
		this.meta = meta;
		this.factory = meta.getFactory();
	}	
	
	public DefaultTableExpression getQuery() {		
		if (query == null) {
			DefaultTableExpression q = new DefaultTableExpression();		
			TableReference tref = new TableReference(meta.getBaseTable());
			q.setFrom(new From(tref));
					
			Select s = new Select();
			
			ElementList<SelectListElement> p = s.getSelectList();

			for (A a : meta.attributes()) {
				Column c = meta.getColumn(a);
				p.add(new ValueElement(new TableColumnExpr(tref, c)));
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
						
			q.setSelect(s);
			this.query = q;
		}
		
		return this.query;		
	}	
		
//	public BaseTable getTable() {
//		return this.factory.getTable();
//	}
	
//	public TableReference getTableReference() {
//		return this.tableRef;
//	}

//	public void setPredicate(EntityPredicate<K, E> predicate) {
//		this.predicate = predicate;
//	}
	
	public DefaultEntityQueryResult<A, R, E> exec(Connection c) 
		throws QueryException {
		return exec(null, c);
	}
	
	public DefaultEntityQueryResult<A, R, E> exec(long offset, Long limit, Connection c) 
		throws QueryException {
		QueryFilter qf = null;	
	
		if (limit != null) {
			qf = new QueryWindowProcessor(null, limit);
		}
					
		if (offset > 0) {
			qf = new QueryOffsetProcessor(qf, offset);
		}
	
		return exec(qf, c);
	}

	public DefaultEntityQueryResult<A, R, E> exec(QueryFilter qf, Connection c) 
		throws QueryException {
			
		Statement st = null;
		ResultSet rs = null;
		DefaultEntityQueryResult<A, R, E> qr = null;
				
		try {
			st = c.createStatement();						
			
			DefaultTableExpression qo = getQuery();
			String qs = qo.generate();
									
			final EntityQueryProcessor ep = getQueryProcessor();
			
			if (qf != null) {
				qf.setInner(ep);				
			}
			
			final QueryProcessor qp = (qf == null) ? ep : qf;
						
			qp.prepare();
			
			long ordinal = 0;
												
			try {
				rs = st.executeQuery(qs);				
				qp.startQuery(rs.getMetaData());
							
				while(rs.next()) {
					qp.process(rs, ++ordinal);
				}
				
				qp.endQuery();
			}
			catch (SQLException e) {
				qp.abort(e);
				throw e;
			}
			finally {				
				qp.finish();								
			}
			
			List<E> el = ep.getContent();
												
			qr = new DefaultEntityQueryResult<A, R, E>(this, el, ordinal);
		} 
		catch (Throwable e) {
			e.printStackTrace();
			throw new QueryException(e.getMessage(), e);
		}
		finally {			
			rs = QueryHelper.doClose(rs);
			st = QueryHelper.doClose(st);			
		}
		
		return qr;
	}

	private EntityQueryProcessor getQueryProcessor() {
		return new EntityQueryProcessor(getQuery());		
	}

	public void setFactory(EntityFactory<A, R, Q, E> factory) {
		this.factory = factory;
	}

	public EntityFactory<A, R, Q, E> getFactory() {
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

		public void set(Entity<A, R, E> dest) {
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
								
		public EntityQueryProcessor(DefaultTableExpression qo) {
			int colno = 0;
									
			BaseTable table = meta.getBaseTable();
			
			List<? extends ColumnName> cl = qo.getSelect().getColumnNameList().getContent();
			
			Extractor[] xa = new Extractor[cl.size()];
			List<AttributeExtractor> awl = new ArrayList<AttributeExtractor>();
									
			for (ColumnName n : qo.getSelect().getColumnNameList().getContent()) {
				colno++;
				Column column = table.getColumn(n);
				
				int sqltype = column.getDataType().getDataType();
				
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

		public List<E> getContent() {
			if (content == null) {
				content = new ArrayList<E>();				
			}

			return content;
		}

		@Override
		public void process(ResultSet rs, long ordinal) throws QueryException {
			try {
				
				EntityFactory<A, R, Q, E> ef = getFactory();
				Entity<A, R, E> e = ef.newInstance();
				
				for (int i = 0; i < this.extractors.length; i++) {
					this.extractors[i].extract(rs);				
				}
				
				for (int i = 0; i < attrs; i++) {
					AttributeExtractor ax = this.attributeWriterList.get(i);
					ax.set(e);
				}	
			}
			catch (Throwable e) {
				throw new QueryException(e.getMessage(), e);
			}			
		}
	}
}
