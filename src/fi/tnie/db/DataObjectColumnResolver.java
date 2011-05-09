/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObject.MetaData;
import fi.tnie.db.expr.ColumnExpr;
import fi.tnie.db.expr.ColumnName;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.Table;

public class DataObjectColumnResolver
 implements ColumnResolver {

	private DataObject.MetaData metaData;
	private Table table;
	
	public DataObjectColumnResolver(Table table, MetaData metaData) {
		super();
		
		if (metaData == null) {
			throw new NullPointerException("metaData");
		}
		
		this.metaData = metaData;
		this.table = table;
	}

	@Override
	public Column getColumn(int index) {
		ColumnExpr ce = metaData.column(index);
		ColumnName cn = ce.getColumnName();				
		Column col = table.columnMap().get(cn);
		return col;
	}

}
