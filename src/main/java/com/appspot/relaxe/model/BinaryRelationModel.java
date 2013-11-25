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

public abstract class BinaryRelationModel<V>
	extends ImmutableBooleanModel {
	
	private MutableBooleanModel result = new MutableBooleanModel();

	public BinaryRelationModel(final ValueModel<V> a, final ValueModel<V> b) {		
		ChangeListener<V> cl = new ChangeListener<V>() {
			@Override
			public void changed(V from, V to) {				
				Boolean newResult = apply(a.get(), b.get());
				result.set(newResult);
			}
		};
		
		if (!a.isConstant()) {
			a.addChangeHandler(cl);
		}
		
		if (!b.isConstant()) {
			b.addChangeHandler(cl);
		}
		
		this.result.set(apply(a.get(), b.get()));
	}

	protected abstract Boolean apply(V a, V b);

	
	@Override
	public Registration addChangeHandler(ChangeListener<Boolean> ch) {
		return this.result.addChangeHandler(ch);
	}
	
	@Override
	public Boolean get() {
		return result.get();
	}

	public static class Gt<V extends Comparable<V>>
		extends BinaryRelationModel<V>
	{
		public Gt(ValueModel<V> a, ValueModel<V> b) {
			super(a, b);
		}
		
		@Override
		protected Boolean apply(V a, V b) {
			if (a == null || b == null) {
				return null;
			}
			
			int result = a.compareTo(b);
			return Boolean.valueOf(result > 0);
		}
	}
	
	public class Eq<T>
		extends BinaryRelationModel<T>
	{
		public Eq(ValueModel<T> a, ValueModel<T> b) {
			super(a, b);
		}
		
		@Override
		protected Boolean apply(T a, T b) {		
			return Boolean.valueOf(a.equals(b));
		}
	}
	
	public static class Lt<V extends Comparable<V>>
		extends BinaryRelationModel<V>
	{
		public Lt(ValueModel<V> a, ValueModel<V> b) {
			super(a, b);
		}
		
		@Override
		protected Boolean apply(V a, V b) {
			if (a == null || b == null) {
				return null;
			}
			
			int result = a.compareTo(b);
			return Boolean.valueOf(result < 0);
		}
	}
	
}
