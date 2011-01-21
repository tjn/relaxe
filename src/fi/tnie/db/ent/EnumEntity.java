/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.EnumMap;
import java.util.Map;

import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class EnumEntity<
	A extends Enum<A> & Identifiable,
	R extends Enum<R> & Identifiable,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
	>
	extends DefaultEntity<A, R, T, E>
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4951609635150994535L;
	
	private Map<R, ReferenceHolder<?, ?, ?, ?>> refs;

	@Override
	protected Map<R, ReferenceHolder<?, ?, ?, ?>> references() {
		if (refs == null) {
			Class<R> nt = getMetaData().getReferenceNameType();
			refs = new EnumMap<R, ReferenceHolder<?, ?, ?, ?>>(nt); 
		}
		
		return refs;		
	}
}
