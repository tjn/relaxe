/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.appspot.relaxe.expr.Identifier;


public abstract class AbstractMetaObjectMap<E extends MetaObject>
	implements ElementMap<E> {
	
	private Environment environment;
	private Map<Identifier, E> content;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractMetaObjectMap() {
	}
	
	protected AbstractMetaObjectMap(Environment environment, Map<Identifier, E> content) {
		super();
		this.environment = environment; 
		this.content = content;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6186221907668223202L;

	@Override
	public boolean contains(Identifier name) {
		return this.content.containsKey(name);
	}

	@Override
	public E get(Identifier name) {
		return this.content.get(name);
	}

	@Override
	public E get(String name) {
		if (name == null) {
			return null;
		}
		
		Identifier identifier = getEnvironment().getIdentifierRules().toIdentifier(name);				
		return this.content.get(identifier);
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@Override
	public Set<Identifier> keySet() {		
		return Collections.unmodifiableSet(this.content.keySet());
	}

	@Override
	public int size() {
		return this.content.size();
	}
	
	@Override
	public boolean isEmpty() {
		return (this.content == null) || this.content.isEmpty();
	}

	@Override
	public Collection<E> values() {
		return Collections.unmodifiableCollection(this.content.values());
	}
}
