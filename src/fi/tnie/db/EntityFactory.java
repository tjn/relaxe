/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Set;

import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;

public interface EntityFactory<K extends Enum<K>, E extends Entity<K, E>> {	
	BaseTable getTable();
	E newInstance() 
		throws InstantiationException, IllegalAccessException;

	EntityQuery<K, E> createEntityQuery(SimpleQueryContext qc);
	Class<K> getColumnNameType();	
	Column getColumn(K k);	
	Set<K> getPKDefinition();
	
	/**
	 * Questionable
	 * @param keys
	 * @param src
	 * @param dest
	 * @throws SQLException
	 */
	void copy(EnumMap<K, Integer> keys, ResultSet src, Entity<K, E> dest) 
		throws SQLException;	

	EnumMap<K, Integer> keys(ResultSetMetaData rs) 
		throws SQLException;
	
		
	/**
	 * Reloads the entity by using a  
	 * 
	 * @param predicate
	 * @return
	 */
	E reload(Entity<K, E> pk, Connection c)
		throws SQLException;
}
