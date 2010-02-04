/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.PrimaryKey;
import fi.tnie.db.meta.Table;

public class DefaultRowFactory<C extends Enum<C>, R extends Row<C>> 
	implements RowFactory<C, R> {
	
	private BaseTable table;
	private Class<C> columnNameType;	
	private Class<R> productType;
	
	private EnumSet<C> pkDefinition;
	
	public DefaultRowFactory() {
		super();	
	}	

	public DefaultRowFactory(BaseTable table, Class<C> columnNameType, Class<R> productType) {
		super();
		
		if (table == null) {
			throw new NullPointerException("'table' must not be null");
		}
		
		if (columnNameType == null) {
			throw new NullPointerException("'columnNameType' must not be null");
		}
		
		if (productType == null) {
			throw new NullPointerException("'productType' must not be null");
		}
				
		this.table = table;
		this.columnNameType = columnNameType;
		this.productType = productType;		
	}
	
	

	@Override
	public BaseTable getTable() {		
		return this.table;
	}		

	@Override
	public RowQuery<C, R> createRowQuery() {
		return new RowQuery<C, R>(this);
	}

	public R newInstance() 
		throws InstantiationException, IllegalAccessException {
		return this.productType.newInstance();
	}

	public Class<C> getColumnNameType() {
		return this.columnNameType;
	}

//	@Override
//	public R reload(Row<C> pk, Connection c) throws SQLException {
//		Map<C, ?> m = pk.getPrimaryKey();
//		
//		if (m == null) {
//			throw new NullPointerException("no primary key null");
//		}
//				
//		RowQuery<C, R> eq = createEntityQuery();		
//		Predicate p = pk.loadedAs(eq);
//				
//		eq.getQuery().getWhere().setSearchCondition(p);		
//		return eq.exec(c).first();
//	}

	@Override
	public Column getColumn(C k) {
		return getTable().columnMap().get(k.toString());
	}

	@Override
	public Set<C> getPKDefinition() {
		if (pkDefinition == null) {
			PrimaryKey pk = getTable().getPrimaryKey();
			
			if (pk == null) {
				throw new IllegalStateException("No primary key in table: " + getTable().getQualifiedName());
			}					
			
			EnumSet<C> ks = EnumSet.allOf(columnNameType);
			
			for (C k : ks) {				
				Column c = getColumn(k);
				
				if (!c.isPrimaryKeyColumn()) {
					ks.remove(k);
				}
			}
			
			this.pkDefinition = ks;
		}
		
		return Collections.unmodifiableSet(pkDefinition);
	}
	
//	public void copy(EnumMap<C, Integer> keys, ResultSet rs, BaseTableRow<C> dest) 
//		throws SQLException {
//		
//		Map<C, Object> m = dest.values();
//					
//		for (Map.Entry<C, Integer> e : keys.entrySet()) {						
//			Object o = rs.getObject(e.getValue().intValue());
//			m.put(e.getKey(), o);
//		}
//	}
		
	public EnumMap<C, Integer> keys(ResultSetMetaData rs) 
		throws SQLException {
		int cc = rs.getColumnCount();
		
		EnumMap<C, Integer> keys = new EnumMap<C, Integer>(columnNameType);
		
		for (int i = 1; i <= cc; i++) {
			String n = rs.getColumnName(i);
			C k = Enum.valueOf(columnNameType, n);
			keys.put(k, new Integer(i));			
		}
		
		return keys;
	}

	@Override
	public EnumMap<C, Integer> columns(ResultSetMetaData rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void copy(EnumMap<C, Integer> keys, ResultSet src, Row<C> dest)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Table getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public R reload(Row<C> pk, Connection c) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	


}
