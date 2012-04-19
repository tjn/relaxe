/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.build;

import fi.tnie.db.meta.Schema;

public interface SchemaFilter {
	SchemaFilter ALL_SCHEMAS = new SchemaFilter() {		
		@Override
		public boolean accept(Schema s) {
			return true;
		}
	};
	
	
	public boolean accept(Schema s);
}
