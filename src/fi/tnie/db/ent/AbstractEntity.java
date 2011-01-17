/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.rpc.StringHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.ReferenceType;


public abstract class AbstractEntity<
	A,
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

	protected abstract Map<A, PrimitiveHolder<?, ?>> values();		
	protected abstract Map<R, ReferenceHolder<?, ?, ?, ?>> references();
	
	public Integer getInteger(A a) throws AttributeNotPresentException {
		return getIntegerHolder(a).value();
	}
	public IntegerHolder getIntegerHolder(A a)
			throws AttributeNotPresentException {
				return (IntegerHolder) holder(a);
			}
	public StringHolder<?> getStringHolder(A a)
			throws AttributeNotPresentException {
				return (StringHolder<?>) holder(a);
			}
	public String getString(A a) throws AttributeNotPresentException {
		return getStringHolder(a).value();
	}
	public Date getDate(A a) throws AttributeNotPresentException {
		return getDateHolder(a).value();
	}
	public DateHolder getDateHolder(A a)
			throws AttributeNotPresentException {
				return (DateHolder) holder(a);
			}

	private PrimitiveHolder<?, ?> holder(A a) throws AttributeNotPresentException {				
		PrimitiveHolder<?, ?> h = value(a);
		
		if (h == null) {
			throw new AttributeNotPresentException(new Ref<A>(a));
		}
		
		return h;
	}
	public void setInt(A a, int v) {
		setInteger(a, Integer.valueOf(v));
	}
	public void setInteger(A a, Integer v) {
		setIntegerHolder(a, IntegerHolder.valueOf(v));
	}
	public void setIntegerHolder(A a, IntegerHolder v) {
		if (v == null) {
			throw new NullPointerException();
		}
		
		set(a, v);
	}
	public void setVarchar(A a, String s) {
		setStringHolder(a, VarcharHolder.valueOf(s));
	}
	public void setStringHolder(A a, StringHolder<?> v) {
		if (v == null) {
			throw new NullPointerException();
		}
		
		set(a, v);
	}
	public void setVarcharHolder(A a, VarcharHolder v) {
		if (v == null) {
			throw new NullPointerException();
		}
		
		set(a, v);
	}
	public void setDate(A a, Date v) {
		setDateHolder(a, DateHolder.valueOf(v));
	}
	public void setDateHolder(A a, DateHolder v) {
		if (v == null) {
			throw new NullPointerException();
		}
		
		set(a, v);
	}
	public boolean isPresent(A a) {
		return value(a) != null;
	}
	public PrimitiveHolder<?, ?> value(A a) {
		if (a == null) {
			throw new NullPointerException("'a' must not be null");
		}
		
		return values().get(a);
	}
	public PrimitiveHolder<?, ?> get(Column column) throws NullPointerException {
		
		if (column == null) {
			throw new NullPointerException("column");
		}
		
		EntityMetaData<A, R, T, E> m = getMetaData();
		
		A a = m.getAttribute(column);
		
		if (a != null) {
			return value(a);			
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
	public void set(A a, PrimitiveHolder<?, ?> value) {
		values().put(a, value);		
	}
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
			PrimitiveHolder<?, ?> v = get(pkcol);
			
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
		return super.toString() + ":" + values() + ";" + this.references();
	}

}
 