/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Comparator;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.CatalogFactory;

public interface Environment {	
	Comparator<Identifier> identifierComparator();
	
	Identifier createIdentifier(String name)
		throws IllegalIdentifierException;
	
	CatalogFactory catalogFactory();
}
