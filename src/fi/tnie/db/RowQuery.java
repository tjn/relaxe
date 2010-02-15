/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import fi.tnie.db.expr.SelectQuery;

/** 
 * @author Administrator
 *
 * @param <C>
 * @param <R>
 */

public class RowQuery<R extends Row>
{	
	private SelectQuery query;
	private RowFactory<R> factory;
								
	public RowQuery(SelectQuery q, RowFactory<R> rf) {
		super();
		
		this.query = q;
		this.factory = rf;
			
//		this.tableRef = new TableReference();
		
	}
	
	
//	public RowQuery(RowFactory<C, R> ef, boolean pkOnly) {
//		this(ef, pkOnly ? ef.getPKDefinition() : null);
//	}
	
//	public DefaultSubselect getQuery() {		
//		if (query == null) {
//			DefaultSubselect q = new DefaultSubselect();		
//			TableReference tref = this.tableRef;
//			q.setFrom(new From(this.tableRef));
//					
//			Select s = new Select();
//			
//			ElementList<SelectListElement> p = s.getSelectList();
//			ColumnMap cm = getTable().columnMap();
//						
//			for (C k : this.projection.keySet()) {
//				Column col = cm.get(k.toString());
//				p.add(new ValueElement(new TableColumnExpr(tref, col)));
//			}
//			
//			q.setSelect(s);
//			this.query = q;
//		}
//		
//		return this.query;		
//	}	
		
//	public BaseTable getTable() {
//		return this.factory.getTable();
//	}
//	
//	public TableReference getTableReference() {
//		return this.tableRef;
//	}

	public RowQueryResult<R> exec(Connection c) 
		throws SQLException {
		return exec(0, Integer.MAX_VALUE, c);
	}

	public RowQueryResult<R> exec(int offset, int pageSize, Connection c) 
		throws SQLException {
			
		Statement st = null;
		ResultSet rs = null;
		RowQueryResult<R> qr = null;
				
		try {
			st = c.createStatement();
										
//			DefaultSubselect qo = getQuery();
			
			SelectQuery qo = getQuery(); 
						
			String qs = qo.generate();
											
			rs = st.executeQuery(qs);			
			List<R> rows = new ArrayList<R>();
			
			int r;
			
			RowFactory<R> rf = getFactory();			
//			EnumMap<C, Integer> keys = this.projection;
						
			InstantiationContext ic = rf.prepare(getQuery());
			
			for(r = 0; rs.next(); r++) {
				
				if (r >= offset && r < offset + pageSize) {
					R row = rf.newInstance(ic);
					
										
					// ef.copy(keys, rs, e);
					rows.add(row);
				}				
			}	
			
			rf.finish(ic);
			
			qr = new RowQueryResult<R>(this, rows, r);
		} 
		catch (InstantiationException e) {
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		finally {
			if (rs != null) {
				rs.close();
			}
			
			if (st != null) {
				st.close();
			}
		}
		
		return qr;
	}

	public SelectQuery getQuery() {
		return query;
	}


	protected RowFactory<R> getFactory() {
		return factory;
	}
	
//	private void copy(Iterable<K> keys, ResultSet rs, E e) 
//		throws SQLException {
//
//		Map<K, Object> m = e.values();
//		int c = 1;
//		
//		for (K k : keys) {
//			Object o = rs.getObject(c++);
//			m.put(k, o);			
//		}
//	}

	
	
	
}
