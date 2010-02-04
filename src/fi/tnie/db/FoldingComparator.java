/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Comparator;

import fi.tnie.db.expr.Identifier;

/**
 * SQL Standard -compatible FoldingComparator.
 * 
 * @author Administrator
 */

public class FoldingComparator
	implements Comparator<Identifier> {

	@Override
	public int compare(Identifier o1, Identifier o2) {
		if (o1 == null || o2 == null) {
			throw new NullPointerException();
		}
		
		String n1 = name(o1);
		String n2 = name(o2);
		
		return n1.compareTo(n2);
	}
	
	protected String fold(String ordinaryIdentifier) {
		return ordinaryIdentifier.toUpperCase();
	}
	
	private String name(Identifier ident) {
		String n = ident.getName();
		return ident.isOrdinary() ? fold(n) : n; 
	}
}
