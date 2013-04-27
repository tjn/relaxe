/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta.impl.pg;

import java.util.Comparator;

import com.appspot.relaxe.expr.AbstractIdentifier;
import com.appspot.relaxe.expr.DelimitedIdentifier;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.IllegalIdentifierException;
import com.appspot.relaxe.meta.Folding;
import com.appspot.relaxe.meta.FoldingComparator;
import com.appspot.relaxe.meta.IdentifierRules;


public class PGIdentifierRules
	implements IdentifierRules {
	
	@Override
	public Comparator<Identifier> comparator() {		
		return FoldingComparator.LOWERCASE;
	}

	@Override
	public DelimitedIdentifier toDelimitedIdentifier(String name)
			throws IllegalIdentifierException {		
		return new DelimitedIdentifier(name);
	}

	@Override
	public Identifier toIdentifier(String name)
			throws IllegalIdentifierException {
		return (name == null) ? null : AbstractIdentifier.create(name, Folding.LOWERCASE);		
	}
}
