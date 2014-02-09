/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.env;

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
