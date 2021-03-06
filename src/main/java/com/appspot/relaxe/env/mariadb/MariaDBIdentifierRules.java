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
/**
 * 
 */
package com.appspot.relaxe.env.mariadb;

import java.util.Comparator;

import com.appspot.relaxe.env.AbstractIdentifierComparator;
import com.appspot.relaxe.env.Folding;
import com.appspot.relaxe.env.IdentifierRules;
import com.appspot.relaxe.env.NullComparator;
import com.appspot.relaxe.expr.AbstractIdentifierRules;
import com.appspot.relaxe.expr.Identifier;


public class MariaDBIdentifierRules
	extends AbstractIdentifierRules
	implements IdentifierRules {

	
	private final IdentifierComparator comparator = new IdentifierComparator();
	
	public static class IdentifierComparator
		extends AbstractIdentifierComparator {
		
		private static final NullComparator.CaseInsensitiveString nameComparator = new NullComparator.CaseInsensitiveString();
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -7036929045437474118L;

		@Override
		protected int compare(String n1, String n2) {
			return nameComparator.compare(n1, n2);
		}

		@Override
		protected String name(Identifier ident) {
			if (ident == null) {
				return null;
			}
			
			String n = ident.getContent();
			return n; 
		}
	}
		
	@Override
	public Folding getFolding() {
		return null;
	}
	
	
	@Override
	public Comparator<Identifier> comparator() {
		return this.comparator;
	}
}