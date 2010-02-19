/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.sql.DatabaseMetaData;
import java.util.Comparator;

import fi.tnie.db.FoldingComparator;
import fi.tnie.db.expr.Identifier;
import fi.tnie.db.meta.CatalogFactory;
import fi.tnie.db.meta.impl.DefaultCatalogFactory;
import fi.tnie.db.meta.impl.DefaultEnvironment;

public class PGEnvironment
	extends DefaultEnvironment {

	public PGEnvironment() {
		
	
	}

	@Override
	public CatalogFactory catalogFactory() {	
		return new DefaultCatalogFactory(this);
	}

	@Override
	protected Comparator<Identifier> createIdentifierComparator() {
		return new FoldingComparator() {
			@Override
			protected String fold(String ordinaryIdentifier) {
				return ordinaryIdentifier.toLowerCase();
			}			
		};
	}
}
