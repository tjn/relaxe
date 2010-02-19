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

public class DefaultEntityFactory<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends DefaultEntity<A, R, Q, E>>
	implements EntityFactory<A, R, Entity<A,R>> {
	
//	private BaseTable table;
//	private Class<A> columnNameType;	
	
	private DefaultEntityMetaData<A, R, Q> meta;
	private Class<E> productType;
		
//	private EnumSet<A> pkDefinition;
	
	public DefaultEntityFactory() {
		super();	
	}	

	public DefaultEntityFactory(DefaultEntityMetaData<A, R, Q> meta, Class<E> productType) {
		super();
		
		if (meta == null) {
			throw new NullPointerException("'meta' must not be null");
		}
		
		if (productType == null) {
			throw new NullPointerException("'productType' must not be null");
		}
				
		this.meta = meta;		
		this.productType = productType;		
	}
	
	

//	@Override
//	public BaseTable getTable() {		
//		return this.table;
//	}		

//	@Override
//	public EntityQuery<A, E> createEntityQuery(SimpleQueryContext qc) {
//		return new EntityQuery<A, E>(qc, this);
//	}

	public E newInstance() 
		throws InstantiationException, IllegalAccessException {
		return this.productType.newInstance();
	}
	
	

//	public Class<A> getColumnNameType() {
//		return this.columnNameType;
//	}

//	@Override
//	public E reload(AbstractEntity<A, E> pk, Connection c) throws SQLException {
//		Map<A, ?> m = pk.getPrimaryKey();
//		
//		if (m == null) {
//			throw new NullPointerException("no primary key null");
//		}
//								
//		SimpleQueryContext qc = new SimpleQueryContext();		
//				
//		EntityQuery<A, E> eq = createEntityQuery(qc);		
//		Predicate p = pk.loadedAs(eq);
//				
//		eq.getQuery().getWhere().setSearchCondition(p);		
//		return eq.exec(c).first();
//	}
//
//	@Override
//	public Column getColumn(A k) {
//		return getTable().columnMap().get(k.toString());
//	}

//	@Override
//	public Set<A> getPKDefinition() {
//		if (pkDefinition == null) {
//			PrimaryKey pk = getTable().getPrimaryKey();
//			
//			if (pk == null) {
//				throw new IllegalStateException("No primary key in table: " + getTable().getQualifiedName());
//			}					
//			
//			EnumSet<A> ks = EnumSet.allOf(columnNameType);
//			
//			for (A k : ks) {				
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
		
//	public EnumMap<A, Integer> keys(ResultSetMetaData rs) 
//		throws SQLException {
//		int cc = rs.getColumnCount();
//		
//		EnumMap<A, Integer> keys = new EnumMap<A, Integer>(columnNameType);
//		
//		for (int i = 1; i <= cc; i++) {
//			String n = rs.getColumnName(i);
//			A k = Enum.valueOf(columnNameType, n);
//			keys.put(k, new Integer(i));			
//		}
//		
//		return keys;
//	}
//
//	@Override
//	public void copy(EnumMap<A, Integer> keys, ResultSet src, AbstractEntity<A, E> dest)
//			throws SQLException {
//		// TODO Auto-generated method stub
//		
//	}



}
