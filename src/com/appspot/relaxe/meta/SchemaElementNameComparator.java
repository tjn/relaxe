/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Comparator;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.SchemaElementName;
import com.appspot.relaxe.expr.SchemaName;


public class SchemaElementNameComparator
	implements Comparator<SchemaElementName> {
	
	private Comparator<Identifier> identifierComparator;
	
	public SchemaElementNameComparator(Comparator<Identifier> identifierComparator) {
		if (identifierComparator == null) {
			throw new NullPointerException("identifierComparator");
		}		
		
		this.identifierComparator = identifierComparator;
	}

	@Override
	public int compare(SchemaElementName n1, SchemaElementName n2) {
		if (n1 == n2) {
			return 0;
		}
				
		SchemaName q1 = n1.getQualifier();
		SchemaName q2 = n2.getQualifier();
		
		int result;		
		
		if (q1 != q2) {
			result = compare(q1.getCatalogName(), q2.getCatalogName());
			
			if (result != 0) {
				return result;
			}
			
			result = compare(q1.getSchemaName(), q2.getSchemaName());
			
			if (result != 0) {
				return result;
			}			
		}
				
		result = this.identifierComparator.compare(n1.getUnqualifiedName(), n2.getUnqualifiedName());		
				
		return result;
	}

	private int compare(Identifier a, Identifier b) {
		return (a == b) ? 0 : this.identifierComparator.compare(a, b);
	}

	
}
