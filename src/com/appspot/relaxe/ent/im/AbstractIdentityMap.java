/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent.im;

import java.util.HashMap;
import java.util.Map;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public abstract class AbstractIdentityMap<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, ?, M, ?>,
	E extends Entity<A, R, T, E, H, ?, M, ?>,
	H extends ReferenceHolder<A, R, T, E, H, M, ?>,
	M extends EntityMetaData<A, R, T, E, H, ?, M, ?>,
	K
>
	implements EntityIdentityMap<A, R, T, E, H, M> {
		
	private Map<K, E> entityMap;
	
	@Override
	public H get(E e) 
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
		
		return u.ref();
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
