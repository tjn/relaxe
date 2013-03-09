/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.env.mysql;

import java.sql.ResultSetMetaData;

import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.ColumnResolver;
import fi.tnie.db.ent.ConstantColumnResolver;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.DefaultGeneratedKeyHandler;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.types.ReferenceType;

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