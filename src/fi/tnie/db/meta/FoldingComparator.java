/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.io.Serializable;
import java.util.Comparator;

import fi.tnie.db.expr.Identifier;

/**
 * SQL Standard -compatible FoldingComparator.
 * 
 * @author Administrator
 */

public class FoldingComparator
	implements Comparator<Identifier>, Serializable
	{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6545832196162079657L;
	
	private static final NullComparator.String nc = new NullComparator.String();

	public FoldingComparator() {
		super();
	}

	@Override
	public int compare(Identifier o1, Identifier o2) {				
		return nc.compare(name(o1), name(o2));
	}
	
	protected String fold(String ordinaryIdentifier) {
		return ordinaryIdentifier.toUpperCase();
	}
	
	private String name(Identifier ident) {
		if (ident == null) {
			return null;
		}
		
		String n = ident.getName();
		return ident.isOrdinary() ? fold(n) : n; 
	}
}
