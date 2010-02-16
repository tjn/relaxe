/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Comparator;

import javax.naming.LinkException;

import fi.tnie.db.expr.Identifier;
import fi.tnie.db.expr.IllegalIdentifierException;
import fi.tnie.db.meta.CatalogFactory;

public interface Environment {
	
	/**
	 * Creates a comparator which corresponds the rules by which 
	 * the equality of the (ordinary and delimited) identifiers are determined 
	 * in this environment.   
	 * @return
	 */
	Comparator<Identifier> identifierComparator();	
		
	/** Creates an identifier from <code>name</code>.
	 * Primarily, an ordinary identifier is created.
	 * If <code>name</code> does not represent ordinary identifier, 
	 * delimited identifier is constructed, if possible.
	 * Otherwise, {@link IllegalIdentifierException} is thrown.  
	 * 
	 * @param Name of the identifier.
	 * @return
	 * @throws IllegalIdentifierException  If <code>name</code> represents neither ordinary nor delimited valid identifier. 
	 * @throws NullPointerException if <code>name</code> is null
	 */
	Identifier createIdentifier(String name)
		throws IllegalIdentifierException, NullPointerException;
	
	
	/** Creates a factory to build entire catalog in this environment. 
	 * 
	 * @return
	 */
	CatalogFactory catalogFactory();
}
