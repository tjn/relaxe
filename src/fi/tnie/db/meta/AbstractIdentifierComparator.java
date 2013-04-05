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

public abstract class AbstractIdentifierComparator
	implements Comparator<Identifier>, Serializable
	{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6545832196162079657L;	
			
	
	
	public AbstractIdentifierComparator() {
		super();
	}

	@Override
	public int compare(Identifier o1, Identifier o2) {				
		return compare(name(o1), name(o2));
	}
	
	protected abstract int compare(String n1, String n2);
			
	protected abstract String name(Identifier ident);
}
