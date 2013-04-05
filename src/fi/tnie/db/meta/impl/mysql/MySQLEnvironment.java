/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.meta.impl.mysql;

import java.util.Comparator;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.AbstractIdentifierComparator;
import fi.tnie.db.meta.NullComparator;
import fi.tnie.db.meta.SerializableEnvironment;
import fi.tnie.db.meta.impl.DefaultEnvironment;

public class MySQLEnvironment 
	extends DefaultEnvironment
	implements SerializableEnvironment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2968383567147338099L;
	
	public static class MySQLIdentifierComparator
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
	protected Comparator<Identifier> createIdentifierComparator() {
		return new MySQLIdentifierComparator();
	}
	
}