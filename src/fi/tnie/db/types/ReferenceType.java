/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.ent.EntityMetaData;

public abstract class ReferenceType<
	R extends ReferenceType<R, M>,
	M extends EntityMetaData<?, ?, R, ?, ?, ?, M>
>
	extends Type<R> {

	@Override
	public final boolean isReferenceType() {
		return true;
	}
	
	public abstract M getMetaData();
}
