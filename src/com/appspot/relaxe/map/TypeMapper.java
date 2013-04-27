/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.map;

import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.Table;

public interface TypeMapper {

	AttributeInfo getAttributeInfo(Table table, Column c);
}
