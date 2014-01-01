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
package com.appspot.relaxe.model.cm;

import com.appspot.relaxe.model.MutableValueModel;

public class DefaultConstrainedMutableValueModel<V>
	extends AbstractConstrainedValueModel<V>
	implements ConstrainedMutableValueModel<V> {

	private V value;
		
	public DefaultConstrainedMutableValueModel(V value) {
		super();
		this.value = value;
	}

	public DefaultConstrainedMutableValueModel() {
		super();
	}

	@Override
	public void set(V newValue) {
		// TODO: silenly fails? should we throw an exception 
		apply(newValue);	
	}

	@Override
	public V get() {
		return this.value;
	}
	
	@Override
	protected Proposition createProposition(V newValue, Proposition impliedBy) {
		return new SimpleProposition<V>(this, newValue, impliedBy) {
			@Override
			protected void apply() {
				DefaultConstrainedMutableValueModel<V> m = DefaultConstrainedMutableValueModel.this;
				m.value = to();
				m.fireIfChanged(from(), to());
			}
		};
	}
	
	@Override
	public MutableValueModel<V> asMutable() {	
		return this;
	}
	
	@Override
	public boolean isNullable() {
		return true;
	}
}
