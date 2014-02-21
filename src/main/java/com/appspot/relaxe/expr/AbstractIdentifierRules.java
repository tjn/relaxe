/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 */
package com.appspot.relaxe.expr;

import java.util.Comparator;

import com.appspot.relaxe.env.Folding;
import com.appspot.relaxe.env.FoldingComparator;
import com.appspot.relaxe.env.IdentifierRules;


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
			StringBuilder buf = new StringBuilder();
			buf.append("length of the identifier ").append(identifier.length());
			buf.append(" exceeds the maximum length ").append(mlen);			
			throw new IllegalIdentifierException(buf.toString());
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
	
	
	@Override
	public SchemaElementName newName(String name) 
			throws IllegalArgumentException, IllegalIdentifierException {	
		String[] tokens = name.split("\\.");
		
		switch (tokens.length) {
		case 1:
			return new SchemaElementName(null, toIdentifier(tokens[0]));
		case 2:
			return new SchemaElementName(null, toIdentifier(tokens[0]), toIdentifier(tokens[1]));
		case 3:
			return new SchemaElementName(toIdentifier(tokens[0]), toIdentifier(tokens[1]), toIdentifier(tokens[2]));
		default:
			throw new IllegalArgumentException("invalid name: " + name);			
		}
	}
}
