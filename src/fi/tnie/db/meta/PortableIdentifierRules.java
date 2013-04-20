/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta;

import java.util.Comparator;

import fi.tnie.db.expr.AbstractIdentifier;
import fi.tnie.db.expr.DelimitedIdentifier;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;

public class PortableIdentifierRules
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
								
		return new DelimitedIdentifier(comparator.fold(name));
	}

	@Override
	public Identifier toIdentifier(String name)
			throws IllegalIdentifierException {
		return (name == null) ? null : AbstractIdentifier.create(name, comparator.getFolding());		
	}
}
