/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import fi.tnie.db.expr.Identifier;

public interface ElementMap<E extends MetaObject>
	extends Serializable {
	public Set<Identifier> keySet();
	public Collection<? extends E> values();
	E get(Identifier name);
	E get(String name);
	
}
