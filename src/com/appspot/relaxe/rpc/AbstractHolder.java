/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.rpc;

import java.io.Serializable;

import com.appspot.relaxe.types.AbstractType;


/** 
 * @author Topi Nieminen <topi.nieminen@gmail.com>
 */
public abstract class AbstractHolder<
	V extends Serializable, 
	T extends AbstractType<T>, 
	H extends Holder<V, T, H>
>
	implements Holder<V, T, H>, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2127849751940907829L;	
	
	
	@Override
	public boolean isNull() {
		return value() == null;
	}
	
	@Override
	public abstract T getType();
	
	/**
	 * 
	 * @param another
	 * @return
	 */
	@Override
	public boolean contentEquals(Holder<?, ?, ?> another) {
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
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (!getClass().equals(obj.getClass())) {
			return false; 
		}
	
		return contentEquals((AbstractHolder<?, ?, ?>) obj);
	}
		 
	
	@Override
	public int hashCode() {
		V v = value();
		int vh = (v == null) ? 0 : v.hashCode();
		int th = getType().hashCode();		
		return vh ^ th;
	}
}
