/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import fi.tnie.db.ent.ColumnResolver;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

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
