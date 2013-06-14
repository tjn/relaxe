/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.samples;

import com.appspot.relaxe.mysql.AbstractMySQLTestCase;

public abstract class AbstractMySQLSamplesTestCase
	extends AbstractMySQLTestCase {

	
	
	@Override
	public String getDatabase() {
		return "samples";
	}
}
