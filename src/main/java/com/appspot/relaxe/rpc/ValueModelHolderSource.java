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
package com.appspot.relaxe.rpc;

import java.io.Serializable;

import com.appspot.relaxe.model.ValueModel;
import com.appspot.relaxe.types.AbstractType;


public abstract class ValueModelHolderSource<V extends Serializable, T extends AbstractType<T>, H extends AbstractHolder<V, T, H>>
	implements HolderSource<V, T, H> {
	
	private ValueModel<V> model;	
	private HolderFactory<V, T, H> factory;
		
	public ValueModelHolderSource(ValueModel<V> model, HolderFactory<V, T, H> factory) {
		super();
		this.model = model;
		this.factory = factory;
	}

	@Override
	public H newHolder() {
		return factory.newHolder(model.get());
	}
}