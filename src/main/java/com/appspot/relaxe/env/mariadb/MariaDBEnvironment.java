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

import com.appspot.relaxe.env.AbstractIdentifierComparator;
import com.appspot.relaxe.env.NullComparator;
import com.appspot.relaxe.env.SerializableEnvironment;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ddl.DefaultDefinition;
import com.appspot.relaxe.meta.Column;

public class MariaDBEnvironment
	implements SerializableEnvironment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2968383567147338099L;
		
	private static MariaDBEnvironment instance;
	private final transient MariaDBIdentifierRules identifierRules = new MariaDBIdentifierRules();
	
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
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private MariaDBEnvironment() {
	}	
	
	public static MariaDBEnvironment environment() {
		if (instance == null) {
			instance = new MariaDBEnvironment();			
		}

		return instance;
	}
	
	@Override
	public MariaDBIdentifierRules getIdentifierRules() {
		return identifierRules;
	}
	
	@Override
	public DefaultDefinition newDefaultDefinition(Column col) {
		return null;
	}
}