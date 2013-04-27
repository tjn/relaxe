/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.build;

import com.appspot.relaxe.meta.Schema;

public interface TableFilter {
	boolean accept(Schema s);
}
