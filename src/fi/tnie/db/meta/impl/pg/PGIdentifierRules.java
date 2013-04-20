/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.util.Comparator;

import fi.tnie.db.expr.AbstractIdentifier;
import fi.tnie.db.expr.DelimitedIdentifier;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.Folding;
import fi.tnie.db.meta.FoldingComparator;
import fi.tnie.db.meta.IdentifierRules;

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
