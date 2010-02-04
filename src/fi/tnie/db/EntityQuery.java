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

public class EntityQuery<K extends Enum<K>, E extends Entity<K, E>>
{		
	private EnumMap<K, Integer> projection;	
	private EntityFactory<K, E> factory;
	private TableReference tableRef;
	
	private DefaultSubselect query; 
							
	public EntityQuery(EntityFactory<K, E> ef, Set<K> projection) {
		super();
		
		this.factory = ef;
		this.tableRef = new TableReference(ef.getTable());
		
		Class<K> cnt = ef.getColumnNameType();
	
		if (projection == null) {
			projection = EnumSet.allOf(cnt);
		}
		else {
			if (projection.isEmpty()) {
				throw new IllegalArgumentException("projection must not be empty");
			}
		}	
		
		EnumMap<K, Integer> pm = new EnumMap<K, Integer>(cnt);
		int co = 1;
		 		
		for (K k : projection) {
			pm.put(k, new Integer(co++));
		}
		
		this.projection = pm;
	}
	
	public EntityQuery(SimpleQueryContext qc, EntityFactory<K, E> ef) {
		this(ef, false);
	}
	
	public EntityQuery(EntityFactory<K, E> ef, boolean pkOnly) {
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
			
			for (K k : this.projection.keySet()) {
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

//	public void setPredicate(EntityPredicate<K, E> predicate) {
//		this.predicate = predicate;
//	}
	
	public EntityQueryResult<K, E> exec(Connection c) 
		throws SQLException {
		return exec(0, Integer.MAX_VALUE, c);
	}

	public EntityQueryResult<K, E> exec(int offset, int pageSize, Connection c) 
		throws SQLException {
			
		Statement st = null;
		ResultSet rs = null;
		EntityQueryResult<K, E> qr = null;
				
		try {
			st = c.createStatement();
						
										
			DefaultSubselect qo = getQuery();
			String qs = qo.generate();
			
			rs = st.executeQuery(qs);			
			List<E> el = new ArrayList<E>();
			
			int r;
			
			EntityFactory<K, E> ef = this.factory;			
			EnumMap<K, Integer> keys = this.projection;
			
			for(r = 0; rs.next(); r++) {				
				if (r >= offset && r < offset + pageSize) {
					E e = ef.newInstance();
					ef.copy(keys, rs, e);
					el.add(e);
				}				
			}	
			
			qr = new EntityQueryResult<K, E>(this, el, r);
		} 
		catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
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
