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
import fi.tnie.db.meta.Table;

public interface RowFactory<C extends Enum<C>, R extends Row<C>> {	
	BaseTable getTable();	
	Table getSource();
	
	R newInstance() 
		throws InstantiationException, IllegalAccessException;

	RowQuery<C, R> createRowQuery();
	Class<C> getColumnNameType();	
	Column getColumn(C c);	
	Set<C> getPKDefinition();
	
	/**
	 * Questionable
	 * @param keys
	 * @param src
	 * @param dest
	 * @throws SQLException
	 */
	void copy(EnumMap<C, Integer> keys, ResultSet src, Row<C> dest) 
		throws SQLException;	

	EnumMap<C, Integer> columns(ResultSetMetaData rs) 
		throws SQLException;
	
		
	/**
	 * Reloads the row by using a primary key.  
	 * 
	 * @param predicate
	 * @return
	 */
	R reload(Row<C> pk, Connection c)
		throws SQLException;
	
}
