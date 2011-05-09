/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.meta.Column;

public class ConstantColumnResolver
	implements ColumnResolver {
	
	private Column column;
	
	public ConstantColumnResolver(Column column) {
		super();
		this.column = column;
	}

	@Override
	public Column getColumn(int index) {
		return this.column;
	}
	

}
