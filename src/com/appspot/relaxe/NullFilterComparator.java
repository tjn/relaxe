/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.util.Comparator;

import com.appspot.relaxe.meta.NullComparator;


public class NullFilterComparator<T>
	extends NullComparator<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4967070673372379093L;
	
	private Comparator<T> inner;
	
	public NullFilterComparator(Comparator<T> inner, boolean nullsAtEnd) {
		super(nullsAtEnd);
		
		if (inner == null) {
			throw new NullPointerException("'inner' must not be null");
		}
		
		this.inner = inner;		
	}
			
	@Override
    public int compareNotNull(T o1, T o2) {
		return this.inner.compare(o1, o2);		
	};
}
