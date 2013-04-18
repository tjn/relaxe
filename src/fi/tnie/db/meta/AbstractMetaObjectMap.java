/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import fi.tnie.db.expr.Identifier;

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
		return this.content.get(getEnvironment().createIdentifier(name));
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
