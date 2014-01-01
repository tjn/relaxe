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
package com.appspot.relaxe.model;

public abstract class AbstractArithmeticModel<N extends Number, A extends Number, B extends Number>
	implements ImmutableValueModel<N> {

	private MutableValueModel<N> result = new DefaultMutableValueModel<N>(null);

	public AbstractArithmeticModel(final ValueModel<A> a, final ValueModel<B> b) {
		if (!a.isConstant()) {						
			a.addChangeHandler(new ChangeListener<A>() {
				@Override
				public void changed(A from, A to) {	
					computeAndAssign(a, b);
				}
			});
		}
		
		if (!b.isConstant()) {
			b.addChangeHandler(new ChangeListener<B>() {
				@Override
				public void changed(B from, B to) {	
					computeAndAssign(a, b);
				}
			});
		}
		
		this.result.set(compute(a.get(), b.get()));
	}
	
	@Override
	public Registration addChangeHandler(ChangeListener<N> ch) {
		return this.result.addChangeHandler(ch);
	}

	protected abstract N compute(A a, B b);
	
	@Override
	public boolean isMutable() {
		return false;
	}
	
	@Override
	public MutableValueModel<N> asMutable() {
		return null;
	}
	
	@Override
	public boolean isConstant() {
		return false;
	}
	
	@Override
	public ImmutableValueModel<N> asImmutable() {
		return this;
	}
	
	@Override
	public ConstantValueModel<N> asConstant() {
		return null;
	}
	
	@Override
	public N get() {
		return this.result.get();
	}

	private void computeAndAssign(final ValueModel<A> a, final ValueModel<B> b) {
		N newResult = compute(a.get(), b.get());
		result.set(newResult);
	}
}
