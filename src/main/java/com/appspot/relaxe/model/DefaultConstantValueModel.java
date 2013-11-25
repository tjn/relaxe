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
package com.appspot.relaxe.model;

public class DefaultConstantValueModel<V>
	extends AbstractValueModel<V>
	implements ConstantValueModel<V> {
	
	private V value;
	
// 	public static final ConstantValueModel<Long> LONG_1 = DefaultConstantValueModel.valueOf(0);

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected DefaultConstantValueModel() {
	}
	
	public DefaultConstantValueModel(V value) {
		super();
		this.value = value;
	}

	private static final Registration registration = new Registration() {		
		@Override
		public void remove() {			
		}
	};
	
	@Override
	public final Registration addChangeHandler(ChangeListener<V> ch) {
		return registration;
	}

	@Override
	public final V get() {
		return this.value;
	}

	@Override
	public final ConstantValueModel<V> asConstant() {
		return this;
	}

	@Override
	public final boolean isConstant() {
		return true;
	}
	
	
	public static <V>
	ConstantValueModel<V> valueOf(V newValue) {
		return new DefaultConstantValueModel<V>(newValue);
	}
	
	public static ConstantValueModel<Long> valueOf(long v) {
		return DefaultConstantValueModel.valueOf(Long.valueOf(v));
	}
	
	public static ConstantValueModel<Integer> valueOf(int v) {
		return DefaultConstantValueModel.valueOf(Integer.valueOf(v));
	}
}
