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

public abstract class ConstrainedBinaryOperatorModel<V, A, B>
	extends AbstractConstrainedValueModel<V> {
	
	private ConstrainedValueModel<A> a; 
	private ConstrainedValueModel<B> b;
	
	public ConstrainedBinaryOperatorModel(final ConstrainedValueModel<A> a, final ConstrainedValueModel<B> b) {
		Constraint c = new Constraint() {				
			@Override
			public void apply(ChangeSet cs, final Proposition p) {
				A av = a.proposed(cs);
				B bv = b.proposed(cs);				
				V newValue = compute(av, bv);	
								
				// rejection of implied proposition also rejects 'p' 
				cs.submit(ConstrainedBinaryOperatorModel.this, newValue, p);				
			}
		};
		
		a.addConstraint(c);		
		b.addConstraint(c);
				
		this.a = a;
		this.b = b;
	}	
		
	public abstract V compute(A a, B b);
	
	@Override
	public V get() {
		return compute(this.a.get(), this.b.get());
	}
	
}
