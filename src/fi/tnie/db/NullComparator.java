/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Comparator;

public class NullComparator<T>
	implements Comparator<T> {
	
	private boolean nullsAtEnd;

	public NullComparator() {
		this(true);
	}
	
	public NullComparator(boolean nullsAtEnd) {
		super();
		this.nullsAtEnd = nullsAtEnd;
	}

	@Override
	public int compare(T o1, T o2) {
		if (o1 == null && o2 == null) {
			return 0;
		}
		
		if (o1 == null) {
			return nullsAtEnd ? -1 : 1;
		}
		
		if (o2 == null) {
			return nullsAtEnd ? 1 : -1;
		}		
		
		return compareNotNull(o1, o2);
	}
	
	public int compareNotNull(T o1, T o2) {
		return 0;
	}
}
