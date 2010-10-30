/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Comparator;

import fi.tnie.db.meta.NullComparator;

public class NullFilterComparator<T>
	extends NullComparator<T> {

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
