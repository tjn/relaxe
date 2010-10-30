/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

public class ReferenceType<R extends ReferenceType<R>>
	extends Type<R> {

	@Override
	public final boolean isReferenceType() {
		return true;
	}
}
