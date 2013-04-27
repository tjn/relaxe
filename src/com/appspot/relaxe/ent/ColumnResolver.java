/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.meta.Column;

public interface ColumnResolver {
	public Column getColumn(int index);
}
