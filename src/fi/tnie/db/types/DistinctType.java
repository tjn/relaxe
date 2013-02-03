/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public abstract class DistinctType<T extends DistinctType<T>>
	extends PrimitiveType<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5821839514528542759L;

	protected DistinctType() {		
	}
	
	/**
	 * The name of the distinct type.
	 * 
	 * @return
	 */
	public abstract String getName();
	
	@Override
	public final int getSqlType() {
		return PrimitiveType.DISTINCT;
	}
}