/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */

package com.appspot.relaxe.mysql.samples;

import com.appspot.relaxe.env.GeneratedKeyHandler;
import com.appspot.relaxe.env.TriggerGeneratedKeyHandler;
import com.appspot.relaxe.env.mysql.MySQLPersistenceContext;

public class MySQLSamplesTriggerTestPersistenceContext
	extends MySQLPersistenceContext {
			
	private GeneratedKeyHandler generatedKeyHandler;

	@Override
	public GeneratedKeyHandler generatedKeyHandler() {
		if (generatedKeyHandler == null) {			
			generatedKeyHandler = new TriggerGeneratedKeyHandler(
				getValueExtractorFactory(),
				"samples",
				"series",				
				"schema_name",
				"table_name",
				"column_name",
				"last_used_value"
			);			
		}

		return generatedKeyHandler;
	}
}
