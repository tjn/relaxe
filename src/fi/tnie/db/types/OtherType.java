/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public abstract class OtherType<T extends OtherType<T>>
	extends PrimitiveType<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5821839514528542759L;

	protected OtherType() {		
	}
	
	/**
	 * The name of the other type.
	 * 
	 * @return
	 */
	public abstract String getName();
	
	@Override
	public final int getSqlType() {
		return PrimitiveType.OTHER;
	}
	
	@Override
	public T asOtherType() {
		return self();
	} 
}
