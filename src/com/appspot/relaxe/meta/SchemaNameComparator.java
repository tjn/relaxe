/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Comparator;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaName;


public class SchemaNameComparator
	implements Comparator<SchemaName> {
	
	private Comparator<Identifier> identifierComparator;
	
	public SchemaNameComparator(Comparator<Identifier> identifierComparator) {
		super();
		this.identifierComparator = identifierComparator;
	}

	@Override
	public int compare(SchemaName a, SchemaName b) {
		if (a == b) {
			return 0;
		}
				
		int result;		
		
		result = compare(a.getCatalogName(), b.getCatalogName());
			
		if (result != 0) {
			return result;
		}
			
		result = compare(a.getSchemaName(), b.getSchemaName());
			
		return result;
	}

	private int compare(Identifier a, Identifier b) {
		return (a == b) ? 0 : this.identifierComparator.compare(a, b);
	}	
}
