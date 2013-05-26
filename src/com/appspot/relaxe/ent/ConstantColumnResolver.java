/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.meta.Column;

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