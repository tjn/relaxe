/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.meta;

import java.util.Comparator;

import com.appspot.relaxe.expr.DelimitedIdentifier;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.IllegalIdentifierException;


public interface IdentifierRules {
	
	Folding getFolding();
	
	/**
	 * Returns a comparator which corresponds the rules by which 
	 * the order and equality of the (ordinary and delimited) identifiers are determined.
	 * 
	 * @return
	 */
	Comparator<Identifier> comparator();	
		
	/** 
	 * Maps a <code>name</code> to an identifier.
	 * 
	 * Primarily, an ordinary identifier is created.
	 * 
	 * If <code>name</code> is not a valid ordinary identifier 
	 * (according the rules represented by implementation), 
	 * delimited identifier is constructed, if possible.
	 * 
	 * Otherwise, {@link IllegalIdentifierException} is thrown.  
	 * 
	 * @param identifier
	 * @return
	 * @throws IllegalIdentifierException  If <code>name</code> can not be interpreted as neither ordinary nor delimited valid identifier. 
	 * @throws NullPointerException If <code>name</code> is null
	 */
	Identifier toIdentifier(String name)
		throws IllegalIdentifierException;
	
	/** 
	 * Maps a <code>name</code> to an delimited identifier.
	 * 
	 * If <code>name</code> is not a valid identifier, {@link IllegalIdentifierException} is thrown.  
	 * 
	 * @param identifier
	 * @return
	 * @throws IllegalIdentifierException  If <code>name</code> can not be interpreted as neither ordinary nor delimited valid identifier. 
	 * @throws NullPointerException If <code>name</code> is null
	 */	
	DelimitedIdentifier toDelimitedIdentifier(String name)
		throws IllegalIdentifierException;
	
	boolean isValidIdentifier(String identifier);
	boolean isValidOrdinaryIdentifier(String identifier);	
	boolean isReservedWord(String word);
}

