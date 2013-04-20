/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.Comparator;

import fi.tnie.db.expr.AbstractIdentifier;
import fi.tnie.db.expr.DelimitedIdentifier;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;

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
