/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.io.Serializable;
import java.util.Comparator;

public abstract class NullComparator<T>
	implements Comparator<T>, Serializable
	{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5072519289660021379L;
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
	
	public abstract int compareNotNull(T o1, T o2);
	
	
	public static class String
		extends NullComparator<java.lang.String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8421830410283422785L;

		@Override
		public int compareNotNull(java.lang.String o1, java.lang.String o2) {
			return o1.compareTo(o2);
		}		
	}
	
	public static class CaseInsensitiveString
		extends NullComparator<java.lang.String> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8421830410283422785L;
	
		@Override
		public int compareNotNull(java.lang.String o1, java.lang.String o2) {
			return o1.compareToIgnoreCase(o2);
		}		
	}
}
