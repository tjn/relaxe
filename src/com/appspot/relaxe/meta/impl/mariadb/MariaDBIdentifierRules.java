/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.meta.impl.mariadb;

import java.util.Comparator;

import com.appspot.relaxe.expr.AbstractIdentifierRules;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.meta.AbstractIdentifierComparator;
import com.appspot.relaxe.meta.IdentifierRules;
import com.appspot.relaxe.meta.NullComparator;


public class MariaDBIdentifierRules
	extends AbstractIdentifierRules
	implements IdentifierRules {

	
	private final IdentifierComparator comparator = new IdentifierComparator();
	
	public static class IdentifierComparator
		extends AbstractIdentifierComparator {
		
		private static final NullComparator.CaseInsensitiveString nameComparator = new NullComparator.CaseInsensitiveString();
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7036929045437474118L;

		@Override
		protected int compare(String n1, String n2) {
			return nameComparator.compare(n1, n2);
		}

		@Override
		protected String name(Identifier ident) {
			if (ident == null) {
				return null;
			}
			
			String n = ident.getName();
			return n; 
		}
	}
	
	
	@Override
	public Comparator<Identifier> comparator() {
		return this.comparator;
	}
}