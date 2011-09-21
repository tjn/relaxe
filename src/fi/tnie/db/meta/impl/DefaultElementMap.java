/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.MetaObject;
import fi.tnie.db.meta.SerializableEnvironment;

public class DefaultElementMap<E extends MetaObject>
	implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4942279888363203444L;
	private TreeMap<Identifier, E> content = null;
	private SerializableEnvironment environment;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected DefaultElementMap() {
	}

	public DefaultElementMap(SerializableEnvironment environment) {
		if (environment == null) {
			throw new NullPointerException("'environment' must not be null");
		}
		
		this.environment = environment;		
		this.content = new TreeMap<Identifier, E>(environment.identifierComparator());				
	}
	
	public Set<Identifier> keySet() {
		return Collections.unmodifiableSet(this.content.keySet());
	}
	
	public Collection<E> values() {
		return Collections.unmodifiableCollection(this.content.values());
	}
	
	public E get(Identifier key) {
		return this.content.get(key);
	}
	
	public E get(String name)
		throws IllegalIdentifierException {		
		Identifier key = (name == null) ?
				null : this.environment.createIdentifier(name);		
		return get(key);
	}	
	
	public boolean add(E value) {
		if (value == null) {
			throw new NullPointerException();
		}
		
		Identifier k = value.getUnqualifiedName();
		E previous = content.get(k);
		
		if (previous != null && previous != value) {
			throw new IllegalArgumentException("Element name is already reserved: " + k.getName()); 
		}
		
		if (previous == null) {
			content.put(k, value);
		}
		
		return (previous == null);
	}
}
