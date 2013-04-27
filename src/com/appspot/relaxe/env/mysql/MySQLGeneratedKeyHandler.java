/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.env.mysql;

import java.sql.ResultSetMetaData;

import com.appspot.relaxe.ValueExtractorFactory;
import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.ColumnResolver;
import com.appspot.relaxe.ent.ConstantColumnResolver;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.env.DefaultGeneratedKeyHandler;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.types.ReferenceType;


public final class MySQLGeneratedKeyHandler 
    extends DefaultGeneratedKeyHandler {
    
	public MySQLGeneratedKeyHandler(ValueExtractorFactory valueExtractorFactory) {
		super(valueExtractorFactory);
	}

	@Override
	protected 
	<
		A extends Attribute,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, ?, ?, M, ?>,
		E extends Entity<A, R, T, E, ?, ?, M, ?>,
		M extends EntityMetaData<A, R, T, E, ?, ?, M, ?>
	>
	ColumnResolver createColumnResolver(ResultSetMetaData meta, M em)
		throws RuntimeException {
//			ResultSet is expected to contain single column: GENERATED_KEY
		BaseTable table = em.getBaseTable();

		// MySQL supports max one auto-increment column per table:
		Column col = findAutoIncrementColumn(table);
		//
		if (col == null) {
			throw new RuntimeException(
					"unable to find AUTO_INCREMENT column from table " +
					em.getBaseTable());
		}

		ConstantColumnResolver cr = new ConstantColumnResolver(col);
		return cr;
	}

	private Column findAutoIncrementColumn(BaseTable tbl) {
		for (Column col : tbl.columnMap().values()) {
			if (Boolean.TRUE.equals(col.isAutoIncrement())) {
				return col;
			}
		}

		return null;
	}
}