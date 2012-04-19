/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class EnumEntity<
	A extends Enum<A> & Attribute,
	R extends Enum<R> & Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
>
	extends DefaultEntity<A, R, T, E, H, F, M, C>
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4951609635150994535L;
	
//	private Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> refs;
//
//	@Override
//	protected Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> references() {
//		super.references()
//		if (refs == null) {
//			Class<R> nt = getMetaData().getReferenceNameType();
//			refs = new EnumMap<R, ReferenceHolder<?, ?, ?, ?, ?, ?>>(nt); 
//		}
//		
//		return refs;		
//	}
}
