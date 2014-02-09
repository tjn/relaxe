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
package com.appspot.relaxe.env;

import java.util.Comparator;

import com.appspot.relaxe.expr.DelimitedIdentifier;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.IllegalIdentifierException;
import com.appspot.relaxe.expr.SchemaElementName;


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

	SchemaElementName newName(String name)
			throws IllegalArgumentException, IllegalIdentifierException;
}

