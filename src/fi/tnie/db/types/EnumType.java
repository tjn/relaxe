/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public abstract class EnumType<T extends EnumType<T, E>, E extends Enum<E> & Enumerable>
	extends OtherType<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5821839514528542759L;

	protected EnumType() {		
	}
	
	/**
	 * The name of the other type.
	 * 
	 * @return
	 */
	@Override
	public abstract String getName();
	
	public abstract Class<E> represents();
	
}
