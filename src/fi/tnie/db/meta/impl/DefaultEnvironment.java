/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl;

import java.util.Comparator;

import fi.tnie.db.DefaultValueExtractorFactory;
import fi.tnie.db.FoldingComparator;
import fi.tnie.db.ValueExtractor;
import fi.tnie.db.ValueExtractorFactory;
import fi.tnie.db.expr.AbstractIdentifier;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.Environment;

public abstract class DefaultEnvironment implements Environment {

	private Comparator<Identifier> identifierComp;
	
	private DefaultValueExtractorFactory valueExtractorFactory; 
	
	@Override
	public abstract CatalogFactory catalogFactory();

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
		
	public ValueExtractorFactory getValueExtractorFactory() {
		if (valueExtractorFactory == null) {
			valueExtractorFactory = new DefaultValueExtractorFactory();			
		}

		return valueExtractorFactory;
	}
	
	
	
}
