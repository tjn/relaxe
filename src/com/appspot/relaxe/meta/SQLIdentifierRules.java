/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Comparator;

import com.appspot.relaxe.expr.AbstractIdentifier;
import com.appspot.relaxe.expr.DelimitedIdentifier;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.IllegalIdentifierException;


public class SQLIdentifierRules
	implements IdentifierRules {
	
	@Override
	public Comparator<Identifier> comparator() {		
		return FoldingComparator.UPPERCASE;
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
		return (name == null) ? null : AbstractIdentifier.create(name, Folding.UPPERCASE);		
	}
}
