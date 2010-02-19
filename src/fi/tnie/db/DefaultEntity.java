/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.EnumMap;

public abstract class DefaultEntity<
	A extends Enum<A> & Identifiable, 
	R extends Enum<R> & Identifiable,
	Q extends Enum<Q> & Identifiable,
	E extends DefaultEntity<A, R, Q, E>
>
	extends AbstractEntity<A, R, Q, E> {
		
	private EntityMetaData<A, R, Q> meta;	
	private EnumMap<A, Object> values;
	private EnumMap<R, Entity<?, ?>> refs;

	@Override
	public EntityMetaData<A, R, Q> getMetaData() {
		return meta;
	}

	void setMetaData(EntityMetaData<A, R, Q> meta) {
		this.meta = meta;
	}
	
	public Object get(A a) {
		return attrs().get(a);		
	};
	
	public void set(A a, Object value) {
		attrs().put(a, value);
	}
	
	private EnumMap<A, Object> attrs() {
		if (values == null) {
			values = new EnumMap<A, Object>(getMetaData().getAttributeNameType()); 
		}
		return values;
	}
	
	private EnumMap<R, Entity<?, ?>> refs() {
		if (refs == null) {
			refs = new EnumMap<R, Entity<?,?>>(getMetaData().getRelationshipNameType()); 
		}
		
		return refs;
	}
	
	public void set(R r, fi.tnie.db.Entity<?,?> ref) {
		refs().put(r, ref);
	}
	
	public fi.tnie.db.Entity<?,?> get(R r) {
		return refs().get(r);
	}
	
	
	
}
