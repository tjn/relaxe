/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import com.appspot.relaxe.ent.ColumnResolver;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObject.MetaData;
import com.appspot.relaxe.expr.ColumnExpr;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Table;

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
		Identifier cn = ce.getColumnName();				
		Column col = table.columnMap().get(cn);
		return col;
	}

}
