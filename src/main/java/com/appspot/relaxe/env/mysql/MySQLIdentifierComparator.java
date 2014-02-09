package com.appspot.relaxe.env.mysql;

import com.appspot.relaxe.env.AbstractIdentifierComparator;
import com.appspot.relaxe.env.NullComparator;
import com.appspot.relaxe.expr.Identifier;

public class MySQLIdentifierComparator
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
		
		String n = ident.getContent();
		return n; 
	}
}