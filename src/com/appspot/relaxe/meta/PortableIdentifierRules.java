/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Comparator;

import com.appspot.relaxe.expr.AbstractIdentifierRules;
import com.appspot.relaxe.expr.DelimitedIdentifier;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.IllegalIdentifierException;


public class PortableIdentifierRules
	extends AbstractIdentifierRules
	implements IdentifierRules {
	
	private static final FoldingComparator comparator = FoldingComparator.LOWERCASE;
		
	@Override
	public Comparator<Identifier> comparator() {		
		return comparator;
	}

	@Override
	public DelimitedIdentifier toDelimitedIdentifier(String name)
			throws IllegalIdentifierException {
		if (name == null) {
			return null;
		}
								
		return new DelimitedIdentifier(name);
	}

	@Override
	public Identifier toIdentifier(String name)
			throws IllegalIdentifierException {				
		return toDelimitedIdentifier(name);		
	}	
	
}
