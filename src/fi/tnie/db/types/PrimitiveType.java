/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public abstract class PrimitiveType<T extends PrimitiveType<T>>
	extends Type<T> {
	
	protected PrimitiveType() {
		super();
	}

	@Override
	public final boolean isReferenceType() {	
		return false;
	}
	
	public abstract int getSqlType();
}
