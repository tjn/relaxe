/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

public interface NodeContainer<N> {
	boolean add(N n);
	boolean remove(N n);
			
}
