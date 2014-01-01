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
package com.appspot.relaxe.expr.op;

import com.appspot.relaxe.expr.Predicate;
import com.appspot.relaxe.expr.SQLKeyword;

public class AndPredicate
	extends BinaryOperator
	implements Predicate {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -13632007707506532L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected AndPredicate() {
	}
	
	public AndPredicate(Predicate left, Predicate right) {
		super(SQLKeyword.AND, left, right);
	}
	
	public static Predicate newAnd(Predicate a, Predicate b) {
		if (a == null) {
			return b;
		}
		
		if (b == null) {
			return a;
		}
		
		return new AndPredicate(a, b);
	}
	
	
	public static Predicate newAnd(Predicate a, Predicate b, Predicate ... tail) {
		Predicate p = newAnd(a, b);
		
		for (int i = 0; i < tail.length; i++) {
			p = newAnd(p, tail[i]);
		}
		
		return p;
	}
}
