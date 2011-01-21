/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fi.tnie.db.ent.value.Key;
import fi.tnie.db.ent.value.Value;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;


public abstract class AbstractEntity<
	A extends Serializable,
	R, 
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
> 
	implements Entity<A, R, T, E>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1538787348338709153L;	

//	public abstract Map<A, PrimitiveHolder<?, ?>> values();		
	protected abstract Map<R, ReferenceHolder<?, ?, ?, ?>> references();
	
	public PrimitiveHolder<?, ?> get(Column column) throws NullPointerException {
		
		if (column == null) {
			throw new NullPointerException("column");
		}
		
		EntityMetaData<A, R, T, E> m = getMetaData();
		
		A a = m.getAttribute(column);
				
		Key<A, ?, ?, ?, E, ?> k = m.getKey(a);
				
		if (k != null) {
			return k.value(self()).getHolder();			
		}	
								
		// column may be part of multiple
		// overlapping foreign-keys:				
		Set<R> rs = m.getReferences(column);
		
		if (rs == null) {
			return null;
		}
	
		Entity<?, ?, ?, ?> ref = null;
		R r = null;
		
		for (R ri : rs) {			
			ReferenceHolder<?, ?, ?, ?> rh = ref(ri);
			
			if (rh != null) {
				ref = rh.value();
							
				if (ref != null) {				
					r = ri;
					break;
				}
			}
		}
				
		if (ref == null) {			
			return null;
		}
		
		ForeignKey fk = m.getForeignKey(r);
		Column fkcol = fk.columns().get(column);		
		return ref.get(fkcol);
	}
//	public void set(A a, PrimitiveHolder<?, ?> value) {
//		values().put(a, value);		
//	}
	
	public void set(R r, ReferenceHolder<?, ?, ?, ?> value) {
		references().put(r, value);		
	}
	public ReferenceHolder<?, ?, ?, ?> ref(R ref) {    	
		return references().get(ref);    	
	}

	/**
	 * Returns a type-safe self-reference. Implementation must return <code>this</code>.
	 *  
	 * @return
	 */
	protected abstract E self();
	public EntityDiff<A, R, T, E> diff(E another) {
		final E self = self();
								
		if (this == another || another == null) {
			return new EmptyEntityDiff<A, R, T, E>(self);
		}
		
		return new EntitySnapshotDiff<A, R, T, E>(self, another);
	}
	
	public Map<Column, PrimitiveHolder<?,?>> getPrimaryKey() {
		Map<Column, PrimitiveHolder<?,?>> pk = new HashMap<Column, PrimitiveHolder<?,?>>(); 
		
		for (Column pkcol : getMetaData().getPKDefinition()) {
			PrimitiveHolder<?,?> v = get(pkcol);
			
			if (v == null) {
				return null;
			}
			
			pk.put(pkcol, v);
		}
		
		return pk;
	}
	
	@Override
	public T getType() {
	 	return getMetaData().getType();
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		EntityMetaData<A, R, T, E> meta = getMetaData();
		
		buf.append(super.toString());
		buf.append(":");
		
		for (A a : meta.attributes()) {
			Key<A, ?, ?, ?, E, ?> key = meta.getKey(a);
			Value<A, ?, ?, ?, E, ?> v = key.value(self());
			buf.append(key.name());
			buf.append("=");
			buf.append(v.get());
			buf.append("; ");
		}
		
		return buf.toString();
	}
	

}
 