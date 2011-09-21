/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.types;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.rpc.ReferenceHolder;

public abstract class ReferenceType<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>	 
>
	extends Type<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7916062747738467637L;

	@Override
	public final boolean isReferenceType() {
		return true;
	}
	
	public abstract M getMetaData();
	
	public abstract T self();
}
