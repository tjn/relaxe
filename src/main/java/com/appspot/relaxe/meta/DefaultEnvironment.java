/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe.meta;

import java.util.Comparator;

import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.expr.ddl.DefaultDefinition;


public abstract class DefaultEnvironment 
	implements SerializableEnvironment {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6518870057408097303L;
	private IdentifierRules identifierRules = new SQLIdentifierRules();
	
	// private DefaultDataTypeMap dataTypeMap = new DefaultDataTypeMap(); 
	
	private final SchemaElementMap<ForeignKey> emptyForeignKeyMap = new EmptyForeignKeyMap(this); 
				
	protected Comparator<Identifier> createIdentifierComparator() {
		return FoldingComparator.UPPERCASE;
	}
	
		
	public SchemaElementMap<ForeignKey> emptyForeignKeyMap()  {
		return this.emptyForeignKeyMap;
	}
	
	@Override
	public IdentifierRules getIdentifierRules() {
		return identifierRules;
	}
	
	
	@Override
	public DefaultDefinition newDefaultDefinition(Column col) {
		return null;
	}
	
}
