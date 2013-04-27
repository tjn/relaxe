/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.appspot.relaxe.expr.Identifier;


public class EmptyForeignKeyMap
	implements SchemaElementMap<ForeignKey> {

	private Environment environment;
	
	public EmptyForeignKeyMap(Environment environment) {
		super();
		this.environment = environment;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1716963444087359039L;

	@Override
	public ForeignKey get(Identifier name) {
		return null;
	}

	@Override
	public ForeignKey get(String name) {
		return null;
	}

	@Override
	public Set<Identifier> keySet() {
		return Collections.emptySet();
	}

	@Override
	public Collection<ForeignKey> values() {
		return Collections.emptyList();
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean contains(Identifier name) {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

}
