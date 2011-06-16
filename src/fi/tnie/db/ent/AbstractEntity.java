/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;


public abstract class AbstractEntity<
	A extends Attribute,
	R extends Reference, 
	T extends ReferenceType<T, M>,	
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>, 
	M extends EntityMetaData<A, R, T, E, H, F, M>
> 
	implements Entity<A, R, T, E, H, F, M>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1538787348338709153L;	

//	public abstract Map<A, PrimitiveHolder<?, ?>> values();		
	protected abstract Map<R, ReferenceHolder<?, ?, ?, ?, ?, ?>> references();
	
	public PrimitiveHolder<?, ?> get(Column column) throws NullPointerException, EntityRuntimeException {
		
		if (column == null) {
			throw new NullPointerException("column");
		}
		
		M m = getMetaData();
		
		A a = m.getAttribute(column);
				
		PrimitiveKey<A, ?, E, ?, ?, ?, ?> k = m.getKey(a);
				
		if (k != null) {
			return k.get(self());			
		}	
								
		// column may be part of multiple
		// overlapping foreign-keys:				
		Set<R> rs = m.getReferences(column);
		
		if (rs == null) {
			return null;
		}
	
		Entity<?, ?, ?, ?, ?, ?, ?> ref = null;
		R r = null;
		
		for (R ri : rs) {			
			ReferenceHolder<?, ?, ?, ?, ?, ?> rh = ref(ri);
			
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
	
//	public void set(R r, ReferenceHolder<?, ?, ?, ?, ?, ?> value) {
//		if (r == null) {
//			throw new NullPointerException("r");
//		}
//		
//		if (value == null) {
//			throw new NullPointerException("value");
//		}
//		
//		if (r.type() != value.getType()) {
//			throw new IllegalArgumentException("type mismatch: expected:" + r.type() + ", argument:" + value.getType());
//		}		
//		
//		references().put(r, value);		
//	}
	
	
	public ReferenceHolder<?, ?, ?, ?, ?, ?> ref(R ref) {    	
		return references().get(ref);    	
	}

	/**
	 * Returns a type-safe self-reference. Implementation must return <code>this</code>.
	 *  
	 * @return
	 */
	public abstract E self();
	
	
	public EntityDiff<A, R, T, E> diff(E another) throws EntityRuntimeException {
		final E self = self();
								
		if (this == another || another == null) {
			return new EmptyEntityDiff<A, R, T, E>(self);
		}
		
		return new EntitySnapshotDiff<A, R, T, E>(self, another);
	}
	
	public Map<Column, PrimitiveHolder<?,?>> getPrimaryKey() throws EntityRuntimeException {
		Map<Column, PrimitiveHolder<?,?>> pk = new HashMap<Column, PrimitiveHolder<?,?>>(); 
		
		for (Column pkcol : getMetaData().getPKDefinition()) {
			PrimitiveHolder<?,?> v = get(pkcol);
			
			if ((v == null) || v.isNull()) {
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
	public final String toString() {
		StringBuffer buf = new StringBuffer();
		
		M meta = getMetaData();
		
		buf.append(super.toString());
		buf.append(":");
		
		if (meta == null) {
			throw new NullPointerException("getMetaData()");
		}
		
		Set<A> as = meta.attributes();
		
		if (as == null) {
			throw new NullPointerException("getMetaData().attributes()");
		}
				
		for (A a : as) {
			PrimitiveKey<A, ?, E, ?, ?, ?, ?> key = meta.getKey(a);
			PrimitiveHolder<?, ?> v = null;
			buf.append(key.name());
			buf.append("=");
			
			try {
				v = key.get(self());
				buf.append(v);
			}
			catch (EntityRuntimeException e) {
				buf.append("[ERROR: ");
				buf.append(e.getMessage());
				buf.append("]");
			}
			
			buf.append("; ");
		}
		
		return buf.toString();
	}

	/**
	 * Returns true if the primary key of this entity has not null components.
	 * @throws EntityRuntimeException 
	 */	
	@Override
	public boolean isIdentified() throws EntityRuntimeException {
		return getPrimaryKey() != null;
	}
}
 