/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

public interface Folding {
	
	public static final Folding UPPERCASE = new Folding() {
		@Override
		public String apply(String ordinaryIdentifier) {
			return ordinaryIdentifier.toUpperCase();
		}		
	};
	
	public static final Folding LOWERCASE = new Folding() {
		public String apply(String ordinaryIdentifier) {
			return ordinaryIdentifier.toLowerCase();
		}
	};		

	String apply(String ordinaryIdentifier);
}
