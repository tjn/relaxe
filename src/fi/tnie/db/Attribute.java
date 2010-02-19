/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

public interface Attribute<A extends Enum<A>, T extends Object> {
	public A name();
	public T getValue();
}
