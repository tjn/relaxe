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
package com.appspot.relaxe.env.mysql;

import com.appspot.relaxe.env.SerializableEnvironment;
import com.appspot.relaxe.expr.ddl.DefaultDefinition;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.DataTypeMap;

public class MySQLEnvironment
	implements SerializableEnvironment {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2968383567147338099L;
		
	private static MySQLEnvironment instance;
	private final transient MySQLIdentifierRules identifierRules = new MySQLIdentifierRules();
	
	/**
	 * No-argument constructor for GWT Serialization
	 */	
	private MySQLEnvironment() {
	}	
	
	public static MySQLEnvironment environment() {
		if (instance == null) {
			instance = new MySQLEnvironment();			
		}

		return instance;
	}
	
	@Override
	public MySQLIdentifierRules getIdentifierRules() {
		return identifierRules;
	}
	
	@Override
	public DefaultDefinition newDefaultDefinition(Column col) {
		return null;
	}	
}