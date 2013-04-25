/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.StringKey;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.meta.PrimaryKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.rpc.StringHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;


public abstract class AbstractEntity<
	A extends Attribute,
	R extends Reference, 
	T extends ReferenceType<A, R, T, E, H, F, M, C>,	
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>, 
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
> 
	implements Entity<A, R, T, E, H, F, M, C>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1538787348338709153L;	

	public PrimitiveHolder<?, ?, ?> get(Column column) throws NullPointerException, EntityRuntimeException {
		
		if (column == null) {
			throw new NullPointerException("column");
		}
		
		M m = getMetaData();
		
		A a = m.getAttribute(column);
		
		if (a != null) {				
			PrimitiveKey<A, E, ?, ?, ?, ?> k = m.getKey(a);
					
			if (k != null) {
				return k.get(self());			
			}	
		}
								
		// column may be part of multiple
		// overlapping foreign-keys:				
		Set<R> rs = m.getReferences(column);
		
		if (rs == null) {
			return null;
		}
	
		Entity<?, ?, ?, ?, ?, ?, ?, ?> ref = null;
		R r = null;
		
		for (R ri : rs) {						
			ReferenceHolder<?, ?, ?, ?, ?, ?, ?> rh = ref(ri);
			
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
		Column fkcol = fk.getReferenced(column);
		return ref.get(fkcol);
	}	

	/**
	 * Returns a type-safe self-reference. Implementation must return <code>this</code>.
	 *  
	 * @return
	 */
	public abstract E self();
	
	
	public EntityDiff<A, R, T, E> diff(E another) {
		final E self = self();
								
		if (this == another || another == null) {
			return new EmptyEntityDiff<A, R, T, E, M>(self);
		}
		
		return new EntitySnapshotDiff<A, R, T, E, M>(self, another);
	}
	
	public Map<Column, PrimitiveHolder<?,?,?>> getPrimaryKey() {
		PrimaryKey pk = getMetaData().getBaseTable().getPrimaryKey();
		
		if (pk == null) {
			return null;
		}
		
		Map<Column, PrimitiveHolder<?,?,?>> vm = new HashMap<Column, PrimitiveHolder<?,?,?>>(2);
				
		for (Column pkcol : pk.getColumnMap().values()) {
			PrimitiveHolder<?,?,?> v = get(pkcol);
			
			if ((v == null) || v.isNull()) {
				return null;
			}
			
			vm.put(pkcol, v);
		}
		
		return vm;
	}
	
	@Override
	public T type() {
	 	return getMetaData().type();
	}
	
	@Override
	public final String toString() {
		StringBuffer buf = new StringBuffer();
		
		M meta = getMetaData();
		
		buf.append("{");
		
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
			PrimitiveKey<A, E, ?, ?, ?, ?> key = meta.getKey(a);
			
			if (key == null) {
				buf.append("<no key for attribute: ");
				buf.append(a);
				buf.append(">");
				continue;
			}					
			
			PrimitiveHolder<?, ?, ?> v = null;
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
			
			buf.append("\n");
		}
		
		Set<R> rs = meta.relationships();
		
		int rc = 0;
		
		for (R r : rs) {
			try {
				EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k = meta.getEntityKey(r);
				ReferenceHolder<?, ?, ?, ?, ?, ?, ?> rh = k.get(self());
				
				if (rh != null) {
					rc++;
					buf.append(r);
					buf.append("=");
					
					if (rh.isNull()) {
						buf.append(rh.toString());
					}
					else {
						buf.append(rh.value());
					}
				}
			}
			catch (EntityRuntimeException e) {
				buf.append("[ERROR: ");
				buf.append(e.getMessage());
				buf.append("]");
			}
			
			buf.append("\n");
		}
		
		buf.append("\n");
		buf.append("ref-count: " + rc);
		buf.append("}");
		
		return buf.toString();
	}

	/**
	 * Returns true if the primary key of this entity has not null components.
	 * @throws EntityRuntimeException 
	 */	
	@Override
	public boolean isIdentified() {
		return getPrimaryKey() != null;
	}
	
	@Override
	public void reset(Iterable<A> as) {		
		M meta = getMetaData();
		
		for (A a : as) {
			PrimitiveKey<A, E, ?, ?, ?, ?> pk = meta.getKey(a);
			pk.reset(self());
		}		
	}

	public <
		VV extends java.io.Serializable, 
		VT extends fi.tnie.db.types.PrimitiveType<VT>, 
		VH extends fi.tnie.db.rpc.PrimitiveHolder<VV, VT, VH>, 
		K extends fi.tnie.db.ent.value.PrimitiveKey<A, E, VV, VT, VH, K>
	> 
	boolean match(K key, E another) {
		VH a = get(key);
		
		if (a == null) {
			return false;
		}
		
		VH b = another.get(key);
		
		if (b == null) {
			return false;
		}
		
		return a.contentEquals(b);		
	};
	
	@Override
	public <
		P extends PrimitiveType<P>,
		SH extends StringHolder<P, SH>,
		K extends StringKey<A, E, P, SH, K>
	>
	SH getString(K k) {
		SH sh = get(k.self());
		return sh;
	}
	
	@Override
	public <
		P extends PrimitiveType<P>,
		SH extends StringHolder<P, SH>,
		K extends StringKey<A, E, P, SH, K>
	>
	void setString(K k, SH s) {		
		set(k.self(), s);
	}
	
	@Override
	public <
		P extends PrimitiveType<P>,
		SH extends StringHolder<P, SH>,
		K extends StringKey<A, E, P, SH, K>
	>
	void setString(K k, String s) {		
		set(k.self(), k.newHolder(s));
	}	


}
 