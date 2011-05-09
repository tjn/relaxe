/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.EnumMap;
import java.util.Map;

import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class EnumEntity<
	A extends Enum<A> & Attribute,
	R extends Enum<R> & Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>	
>
	extends DefaultEntity<A, R, T, E, H, F, M>
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4951609635150994535L;
	
	private Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> refs;

	@Override
	protected Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> references() {
		if (refs == null) {
			Class<R> nt = getMetaData().getReferenceNameType();
			refs = new EnumMap<R, ReferenceHolder<?, ?, ?, ?, ?, ?>>(nt); 
		}
		
		return refs;		
	}
}
