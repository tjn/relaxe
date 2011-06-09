/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

import fi.tnie.db.types.Type;

/** 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */
public abstract class Holder<V extends Serializable, T extends Type<T>>
	implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2127849751940907829L;	
	public abstract V value();
	
	public boolean isNull() {
		return value() == null;
	}
	
	public abstract T getType();
	
	/**
	 * 
	 * @param another
	 * @return
	 */
	public boolean contentEquals(Holder<?, ?> another) {
		if (another == this) {
			return true;
		}
		
		V a = value();
		Object b = another.value();
		
		boolean result = 
				(a == null) ? 
				(b == null) : 
				(b == null) ? false : a.equals(b);
				
		return result;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			throw new NullPointerException("obj");
		}
		
		if (!getClass().equals(obj.getClass())) {
			return false; 
		}
	
		return contentEquals((Holder<?, ?>) obj);
	}
	
	@Override
	public int hashCode() {
		V v = value();
		int vh = (v == null) ? 0 : v.hashCode();
		int th = getType().hashCode();		
		return vh ^ th;
	}
}
