/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import com.appspot.relaxe.expr.Identifier;


public interface ElementMap<E extends MetaObject>
	extends Serializable {
	
	Environment getEnvironment();	
	Set<Identifier> keySet();
	Collection<E> values();
	E get(Identifier name);
	E get(String name);	
	int size();	
	boolean isEmpty();
	boolean contains(Identifier name);
}
