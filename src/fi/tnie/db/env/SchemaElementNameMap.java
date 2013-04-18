/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.env;

import java.util.Comparator;
import java.util.TreeMap;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.SchemaElementName;

public class SchemaElementNameMap<E> 
	extends TreeMap<SchemaElementName, E> {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6122739665197084520L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected SchemaElementNameMap() {
	}

	public SchemaElementNameMap(Comparator<Identifier> icmp) {
		super(new SchemaElementNameComparator(icmp));	
	}
}
