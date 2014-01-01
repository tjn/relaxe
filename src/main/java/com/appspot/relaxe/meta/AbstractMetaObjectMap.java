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
package com.appspot.relaxe.meta;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.appspot.relaxe.expr.Identifier;


public abstract class AbstractMetaObjectMap<E extends MetaObject>
	implements ElementMap<E> {
	
	private Environment environment;
	private Map<Identifier, E> content;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AbstractMetaObjectMap() {
	}
	
	protected AbstractMetaObjectMap(Environment environment, Map<Identifier, E> content) {
		super();
		this.environment = environment; 
		this.content = content;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6186221907668223202L;

	@Override
	public boolean contains(Identifier name) {
		return this.content.containsKey(name);
	}

	@Override
	public E get(Identifier name) {
		return this.content.get(name);
	}

	@Override
	public E get(String name) {
		if (name == null) {
			return null;
		}
		
		Identifier identifier = getEnvironment().getIdentifierRules().toIdentifier(name);				
		return this.content.get(identifier);
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@Override
	public Set<Identifier> keySet() {		
		return Collections.unmodifiableSet(this.content.keySet());
	}

	@Override
	public int size() {
		return this.content.size();
	}
	
	@Override
	public boolean isEmpty() {
		return (this.content == null) || this.content.isEmpty();
	}

	@Override
	public Collection<E> values() {
		return Collections.unmodifiableCollection(this.content.values());
	}
}
