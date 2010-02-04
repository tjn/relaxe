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
import java.util.Map;
import java.util.Set;

import fi.tnie.db.expr.Predicate;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.PrimaryKey;

public class DefaultEntityFactory<K extends Enum<K>, E extends Entity<K, E>> 
	implements EntityFactory<K, E> {
	
	private BaseTable table;
	private Class<K> columnNameType;	
	private Class<E> productType;
	
	private EnumSet<K> pkDefinition;
	
	public DefaultEntityFactory() {
		super();	
	}	

	public DefaultEntityFactory(BaseTable table, Class<K> columnNameType, Class<E> productType) {
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
	public EntityQuery<K, E> createEntityQuery(SimpleQueryContext qc) {
		return new EntityQuery<K, E>(qc, this);
	}

	public E newInstance() 
		throws InstantiationException, IllegalAccessException {
		return this.productType.newInstance();
	}

	public Class<K> getColumnNameType() {
		return this.columnNameType;
	}

	@Override
	public E reload(Entity<K, E> pk, Connection c) throws SQLException {
		Map<K, ?> m = pk.getPrimaryKey();
		
		if (m == null) {
			throw new NullPointerException("no primary key null");
		}
								
		SimpleQueryContext qc = new SimpleQueryContext();		
				
		EntityQuery<K, E> eq = createEntityQuery(qc);		
		Predicate p = pk.loadedAs(eq);
				
		eq.getQuery().getWhere().setSearchCondition(p);		
		return eq.exec(c).first();
	}

	@Override
	public Column getColumn(K k) {
		return getTable().columnMap().get(k.toString());
	}

	@Override
	public Set<K> getPKDefinition() {
		if (pkDefinition == null) {
			PrimaryKey pk = getTable().getPrimaryKey();
			
			if (pk == null) {
				throw new IllegalStateException("No primary key in table: " + getTable().getQualifiedName());
			}					
			
			EnumSet<K> ks = EnumSet.allOf(columnNameType);
			
			for (K k : ks) {				
				Column c = getColumn(k);
				
				if (!c.isPrimaryKeyColumn()) {
					ks.remove(k);
				}
			}
			
			this.pkDefinition = ks;
		}
		
		return Collections.unmodifiableSet(pkDefinition);
	}
	
//	public void copy(EnumMap<K, Integer> keys, ResultSet rs, Entity<K, E> dest) 
//		throws SQLException {
//		
//		Map<K, Object> m = dest.values();
//					
//		for (Map.Entry<K, Integer> e : keys.entrySet()) {						
//			Object o = rs.getObject(e.getValue().intValue());
//			m.put(e.getKey(), o);
//		}
//	}
		
	public EnumMap<K, Integer> keys(ResultSetMetaData rs) 
		throws SQLException {
		int cc = rs.getColumnCount();
		
		EnumMap<K, Integer> keys = new EnumMap<K, Integer>(columnNameType);
		
		for (int i = 1; i <= cc; i++) {
			String n = rs.getColumnName(i);
			K k = Enum.valueOf(columnNameType, n);
			keys.put(k, new Integer(i));			
		}
		
		return keys;
	}

	@Override
	public void copy(EnumMap<K, Integer> keys, ResultSet src, Entity<K, E> dest)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}



}
