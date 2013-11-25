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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.appspot.relaxe.expr.Identifier;


public class EmptyForeignKeyMap
	implements SchemaElementMap<ForeignKey> {

	private Environment environment;
	
	public EmptyForeignKeyMap(Environment environment) {
		super();
		this.environment = environment;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1716963444087359039L;

	@Override
	public ForeignKey get(Identifier name) {
		return null;
	}

	@Override
	public ForeignKey get(String name) {
		return null;
	}

	@Override
	public Set<Identifier> keySet() {
		return Collections.emptySet();
	}

	@Override
	public Collection<ForeignKey> values() {
		return Collections.emptyList();
	}

	@Override
	public Environment getEnvironment() {
		return this.environment;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean contains(Identifier name) {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

}
