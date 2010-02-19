/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.EnumMap;

import fi.tnie.db.expr.QueryExpression;
import fi.tnie.db.meta.BaseTable;

public class DefaultRowFactory<C extends Enum<C>, R extends MutableRow> 
	implements RowFactory<R, InstantiationContext<R>> {
	
	private Class<C> columnNameType;	
	private Class<R> productType;
	
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
				
		this.columnNameType = columnNameType;
		this.productType = productType;		
	}
	
	public Class<C> getColumnNameType() {
		return this.columnNameType;
	}


//	@Override
//	public Column getColumn(C k) {
//		return getTable().columnMap().get(k.toString());
//	}
//
//	@Override
//	public Set<C> getPKDefinition() {
//		if (pkDefinition == null) {
//			PrimaryKey pk = getTable().getPrimaryKey();
//			
//			if (pk == null) {
//				throw new IllegalStateException("No primary key in table: " + getTable().getQualifiedName());
//			}					
//			
//			EnumSet<C> ks = EnumSet.allOf(columnNameType);
//			
//			for (C k : ks) {				
//				Column c = getColumn(k);
//				
//				if (!c.isPrimaryKeyColumn()) {
//					ks.remove(k);
//				}
//			}
//			
//			this.pkDefinition = ks;
//		}
//		
//		return Collections.unmodifiableSet(pkDefinition);
//	}
	
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
	
	public class InstContext
		implements InstantiationContext<R> {

		private QueryExpression query = null;
		
		protected InstContext(QueryExpression query) {
			super();
			
			if (query == null) {
				throw new NullPointerException("'query' must not be null");
			}
			
			this.query = query;
		}
	
		@Override
		public QueryExpression getQueryExpression() {
			return query;
		}	
	}



	@Override
	public R newInstance(InstantiationContext<R> ictx, ResultSet rs)
			throws InstantiationException, IllegalAccessException, SQLException {
		R result = newInstance(ictx);
		
		int cc = rs.getMetaData().getColumnCount();
		
		for (int c = 1; c <= cc; c++) {
			result.set(c, rs.getObject(c));			
		}				
		
		return result;
	}

	@Override
	public R newInstance(InstantiationContext<R> ictx)
			throws InstantiationException, IllegalAccessException {
		return this.productType.newInstance();
	}

	@Override
	public InstantiationContext<R> prepare(QueryExpression query) {
		return new InstContext(query);
	}

	@Override
	public void finish(InstantiationContext<R> ictx) {
		
	}
	

}
