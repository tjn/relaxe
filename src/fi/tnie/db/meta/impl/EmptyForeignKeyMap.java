/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.SchemaElementMap;

public class EmptyForeignKeyMap
	implements SchemaElementMap<ForeignKey>, Serializable {

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
	public Collection<? extends ForeignKey> values() {
		return Collections.emptyList();
	}

}
