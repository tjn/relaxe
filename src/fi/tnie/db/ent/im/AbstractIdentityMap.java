/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.im;

import java.util.HashMap;
import java.util.Map;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractIdentityMap<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, ?, ?, ?>,
	E extends Entity<A, R, T, E, ?, ?, ?>,
	K
>
	implements EntityIdentityMap<A, R, T, E> {
		
	private Map<K, E> entityMap;
	
	public E get(E e) 
		throws EntityRuntimeException {
		if (e == null) {
			throw new NullPointerException("e");
		}
		
		K k = identify(e);
		
		if (k == null) {
			return null;
		}
		
		Map<K, E> m = getEntityMap();		
		E u = m.get(k);
		
		if (u == null) {
			m.put(k, e);
			u = e;
		}
		
		return u;
	}
	
	public Map<K, E> getEntityMap() {
		if (entityMap == null) {
			entityMap = new HashMap<K, E>();			
		}

		return entityMap;
	}
	
	
	protected abstract K identify(E src) 
		throws EntityRuntimeException;

}
