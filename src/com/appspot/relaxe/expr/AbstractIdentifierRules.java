/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.expr;

import java.util.Comparator;

import com.appspot.relaxe.meta.Folding;
import com.appspot.relaxe.meta.FoldingComparator;
import com.appspot.relaxe.meta.IdentifierRules;


public class AbstractIdentifierRules
	implements IdentifierRules {
	
	/**
	 * This does not seem to allow unicode -characters 
	 * (If it is changed, keep in in mind it should be GWT -compatible (emulated java.lang.String.matches(String s)). 
	 */
	private static final String ORDINARY_PATTERN = "[A-Za-z][A-Za-z0-9_]*";
	
	@Override
	public Comparator<Identifier> comparator() {		
		return FoldingComparator.UPPERCASE;
	}

	@Override
	public Folding getFolding() {		
		return Folding.UPPERCASE;
	}

	@Override
	public DelimitedIdentifier toDelimitedIdentifier(String name)
			throws IllegalIdentifierException {
				
		return newDelimitedIdentifier(name);
	}

	private final DelimitedIdentifier newDelimitedIdentifier(String name)
			throws IllegalIdentifierException {		
		return new DelimitedIdentifier(name);
	}

	@Override
	public Identifier toIdentifier(String s)
			throws IllegalIdentifierException {
		
		validate(s);
		Identifier oi = newOrdinaryIdentifier(s);		
		return (oi == null) ? newDelimitedIdentifier(s) : oi;		
	}

	private final OrdinaryIdentifier newOrdinaryIdentifier(String s)
		throws IllegalIdentifierException {
		
		if (qualifiesAsOrdinary(s)) {
			Folding f = getFolding();
			String nn = (f == null) ? s : f.apply(s);
			return new OrdinaryIdentifier(nn);
		}
		
		return null;
	}
	
	@Override
	public boolean isValidIdentifier(String s) {
		return (s != null) &&
			   (s.length() > 0) &&
			   (s.length() <= getMaxLength());
	}
	
	@Override
	public boolean isValidOrdinaryIdentifier(String s) {
		return isValidIdentifier(s) && qualifiesAsOrdinary(s);				
	}
		
	private void validate(String identifier)
		throws IllegalIdentifierException {
		
		if (identifier == null) {
			throw new NullPointerException("identifier");
		}
		
		int len = identifier.length();
		
		if (len == 0) {
			throw new IllegalIdentifierException("identifier must not be empty");
		}
		
		int mlen = getMaxLength();
		
		if (len > mlen) {
			throw new IllegalIdentifierException(
					String.format(
							"length of the identifier %s exceeds the maximum length %s", 
							identifier.length(), mlen));
		}
	}
	
	public int getMaxLength() {
		return 128;
	}
	
	protected boolean qualifiesAsOrdinary(String word) {
		return word.matches(ORDINARY_PATTERN) && (!isReservedWord(word));
	}

	@Override
	public boolean isReservedWord(String word) {
		return SQLKeyword.isKeyword(word);
	}
}
