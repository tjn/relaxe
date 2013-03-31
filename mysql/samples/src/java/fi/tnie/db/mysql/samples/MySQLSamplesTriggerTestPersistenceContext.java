/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */

package fi.tnie.db.mysql.samples;

import fi.tnie.db.env.GeneratedKeyHandler;
import fi.tnie.db.env.TriggerGeneratedKeyHandler;
import fi.tnie.db.env.mysql.MySQLPersistenceContext;

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
