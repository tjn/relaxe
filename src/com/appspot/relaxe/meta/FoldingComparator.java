/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import com.appspot.relaxe.expr.Identifier;

/**
 * SQL Standard -compatible FoldingComparator.
 * 
 * @author Administrator
 */

public abstract class FoldingComparator
	extends AbstractIdentifierComparator {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 6545832196162079657L;	
	private static final NullComparator.String nameComparator = new NullComparator.String();
		
	public static final FoldingComparator UPPERCASE = new FoldingComparator() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8134439617694820125L;

		@Override
		public Folding getFolding() {
			return Folding.UPPERCASE;
		}
	};
	
	public static final FoldingComparator LOWERCASE = new FoldingComparator() {
		private static final long serialVersionUID = 8134439617694820125L;

		@Override
		public Folding getFolding() {
			return Folding.LOWERCASE;
		}
	};
	
	public abstract Folding getFolding();

	public FoldingComparator() {
		super();
	}

		
	@Override
	protected int compare(String n1, String n2) {
		return nameComparator.compare(n1, n2);
	}
	
	protected String fold(String ordinaryIdentifier) {
		return getFolding().apply(ordinaryIdentifier);
	}
	
	@Override
	protected String name(Identifier ident) {
		if (ident == null) {
			return null;
		}
		
		String n = ident.getName();
		return ident.isOrdinary() ? fold(n) : n; 
	}
}
