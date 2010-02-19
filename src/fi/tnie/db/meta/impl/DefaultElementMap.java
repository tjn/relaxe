/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.MetaObject;

public class DefaultElementMap<E extends MetaObject>
{
	private TreeMap<Identifier, E> content = null;
	private Catalog catalog;

	public DefaultElementMap(Catalog catalog) {
		if (catalog == null) {
			throw new NullPointerException("'catalog' must not be null");
		}
		
		this.catalog = catalog;		
		this.content = new TreeMap<Identifier, E>(catalog.getEnvironment().identifierComparator());				
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
		return get(catalog.getEnvironment().createIdentifier(name));
	}	
	
	public boolean add(E value) {
		if (value == null) {
			throw new NullPointerException();
		}
		
		Identifier k = value.getUnqualifiedName();
		E previous = content.get(k);
		
		if (previous != null && previous != value) {
			throw new IllegalArgumentException("Element name is already reserved: " + k); 
		}
		
		if (previous == null) {
			content.put(k, value);
		}
		
		return (previous == null);
	}
}
