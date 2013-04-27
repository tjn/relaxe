/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.build;

import com.appspot.relaxe.meta.Schema;

public interface SchemaFilter {
	SchemaFilter ALL_SCHEMAS = new SchemaFilter() {		
		@Override
		public boolean accept(Schema s) {
			return true;
		}
	};
	
	
	public boolean accept(Schema s);
}
