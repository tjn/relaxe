/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class PrimitiveType<T extends Type<T>>
	extends Type<T> {

	protected PrimitiveType() {
		super();
	}

	public PrimitiveType(int sqlType) {
		super(sqlType);
	}
	
	@Override
	public final boolean isReferenceType() {	
		return false;
	}
}
