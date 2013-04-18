/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.Map;
import java.util.TreeMap;

import fi.tnie.db.expr.Identifier;

public abstract class MetaObjectBuilder {

	private Environment environment;

	public MetaObjectBuilder(Environment environment) {
		super();
		
		if (environment == null) {
			throw new NullPointerException("environment");
		}
		
		this.environment = environment;
	}
	
		
	
	public Environment getEnvironment() {
		return environment;
	}
		
	protected boolean equal(Identifier a, Identifier b) {		
		return (getEnvironment().identifierComparator().compare(a, b) == 0);		
	}
	
	public <E> Map<Identifier, E> createMap() {	
		return new TreeMap<Identifier, E>(getEnvironment().identifierComparator());
	}
}
