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

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import com.appspot.relaxe.env.Environment;
import com.appspot.relaxe.expr.Identifier;


public abstract class MetaObjectBuilder {

	private Environment environment;
	private Comparator<Identifier> comparator;
	

	public MetaObjectBuilder(Environment environment) {
		super();
		
		if (environment == null) {
			throw new NullPointerException("environment");
		}
		
		this.environment = environment;
		this.comparator = environment.getIdentifierRules().comparator();
	}
	
	public Environment getEnvironment() {
		return environment;
	}
		
	protected boolean equal(Identifier a, Identifier b) {		
		return (comparator.compare(a, b) == 0);		
	}
	
	public <E> Map<Identifier, E> createMap() {	
		return new TreeMap<Identifier, E>(this.comparator);
	}
}
