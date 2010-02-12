/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import fi.tnie.db.expr.DefaultSubselect;
import fi.tnie.db.expr.ElementList;
import fi.tnie.db.expr.From;
import fi.tnie.db.expr.Select;
import fi.tnie.db.expr.SelectListElement;
import fi.tnie.db.expr.ValueElement;
import fi.tnie.db.expr.TableColumnExpr;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.impl.ColumnMap;

/** 
 * @author Administrator
 *
 * @param <C>
 * @param <R>
 */

public class RowQuery<R extends Row>
{		
	private EnumMap<C, Integer> projection;	
	private RowFactory<C, R> factory;
	private TableReference tableRef;
	
	private DefaultSubselect query; 
							
	public RowQuery(RowFactory<C, R> rf, Set<C> projection) {
		super();
		
		this.factory = rf;
		this.tableRef = new TableReference(rf.getTable());
		
		Class<C> cnt = rf.getColumnNameType();
	
		if (projection == null) {
			projection = EnumSet.allOf(cnt);
		}
		else {
			if (projection.isEmpty()) {
				throw new IllegalArgumentException("projection must not be empty");
			}
		}	
		
		EnumMap<C, Integer> pm = new EnumMap<C, Integer>(cnt);
		int co = 1;
		 		
		for (C k : projection) {
			pm.put(k, new Integer(co++));
		}
		
		this.projection = pm;
	}
	
	public RowQuery(RowFactory<C, R> ef) {
		this(ef, false);
	}
	
	public RowQuery(RowFactory<C, R> ef, boolean pkOnly) {
		this(ef, pkOnly ? ef.getPKDefinition() : null);
	}
	
	public DefaultSubselect getQuery() {		
		if (query == null) {
			DefaultSubselect q = new DefaultSubselect();		
			TableReference tref = this.tableRef;
			q.setFrom(new From(this.tableRef));
					
			Select s = new Select();
			
			ElementList<SelectListElement> p = s.getSelectList();
			ColumnMap cm = getTable().columnMap();
						
			for (C k : this.projection.keySet()) {
				Column col = cm.get(k.toString());
				p.add(new ValueElement(new TableColumnExpr(tref, col)));
			}
			
			q.setSelect(s);
			this.query = q;
		}
		
		return this.query;		
	}	
		
	public BaseTable getTable() {
		return this.factory.getTable();
	}
	
	public TableReference getTableReference() {
		return this.tableRef;
	}

	public RowQueryResult<C, R> exec(Connection c) 
		throws SQLException {
		return exec(0, Integer.MAX_VALUE, c);
	}

	public RowQueryResult<C, R> exec(int offset, int pageSize, Connection c) 
		throws SQLException {
			
		Statement st = null;
		ResultSet rs = null;
		RowQueryResult<C, R> qr = null;
				
		try {
			st = c.createStatement();
										
			DefaultSubselect qo = getQuery();
			String qs = qo.generate();
			
			rs = st.executeQuery(qs);			
			List<R> el = new ArrayList<R>();
			
			int r;
			
			RowFactory<C, R> ef = this.factory;			
			EnumMap<C, Integer> keys = this.projection;
			
			for(r = 0; rs.next(); r++) {				
				if (r >= offset && r < offset + pageSize) {
					R e = ef.newInstance();
					ef.copy(keys, rs, e);
					el.add(e);
				}				
			}	
			
			qr = new RowQueryResult<C, R>(this, el, r);
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

	protected RowFactory<C, R> getFactory() {
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
