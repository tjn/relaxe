/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Comparator;

import fi.tnie.db.expr.AbstractIdentifier;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.Environment;
import fi.tnie.db.meta.FoldingComparator;

public abstract class DefaultEnvironment 
	implements Environment {

	private Comparator<Identifier> identifierComp;	
			
	@Override
	public Identifier createIdentifier(String name)
			throws IllegalIdentifierException {
		return (name == null) ? null : AbstractIdentifier.create(name);
	}

	@Override
	public final Comparator<Identifier> identifierComparator() {
		if (identifierComp == null) {
			identifierComp = createIdentifierComparator();			
		}

		return identifierComp;		
	}	
	
	protected Comparator<Identifier> createIdentifierComparator() {
		return new FoldingComparator();
	}		
}
