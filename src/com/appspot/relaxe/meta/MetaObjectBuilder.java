/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import com.appspot.relaxe.expr.Identifier;


public abstract class MetaObjectBuilder {

	private Environment environment;
	private Comparator<Identifier> comparator;
	

	public MetaObjectBuilder(Environment environment) {
		super();
		
		if (environment == null) {
			throw new NullPointerException("environment");
		}
		
		this.environment = environment;
		this.comparator = environment.getIdentifierRules().comparator();
	}
	
	public Environment getEnvironment() {
		return environment;
	}
		
	protected boolean equal(Identifier a, Identifier b) {		
		return (comparator.compare(a, b) == 0);		
	}
	
	public <E> Map<Identifier, E> createMap() {	
		return new TreeMap<Identifier, E>(this.comparator);
	}
}
