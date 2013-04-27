/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.appspot.relaxe.ent.ColumnResolver;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Table;


public class ResultSetColumnResolver
 implements ColumnResolver {

	private ResultSetMetaData metaData;
	private Table table;
	
	public ResultSetColumnResolver(Table table, ResultSetMetaData metaData) {
		super();
		
		if (metaData == null) {
			throw new NullPointerException("metaData");
		}
		
		this.metaData = metaData;
		this.table = table;
	}

	@Override
	public Column getColumn(int index) {
		try {
			String label = metaData.getColumnLabel(index);
			Column col = table.columnMap().get(label);
			return col;
		}
		catch (SQLException e) {
			// TODO: fix
			throw new RuntimeException(e);
		}
	}

}
