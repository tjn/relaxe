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

import com.appspot.relaxe.types.IntegerType;
import com.appspot.relaxe.types.PrimitiveType;


public class IntegerHolder
	extends AbstractPrimitiveHolder<Integer, IntegerType, IntegerHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -7895663698067736815L;
	private Integer value;	
	public static final IntegerHolder NULL_HOLDER = new IntegerHolder();
		
//	public static final IntegerType TYPE = IntegerType.TYPE;
	
	private static IntegerHolder[] small;
		
	static {
		small = new IntegerHolder[256];
		                          
		for (int i = 0; i < small.length; i++) {
			small[i] = new IntegerHolder(i);			
		}
	}
		
	public static IntegerHolder valueOf(int v) {
		/**
		 * Possibly cache small ints later like java.lang.Integer
		 */
		return (v >= 0 && v < small.length) ? small[v] : new IntegerHolder(v);		
		// return new IntegerHolder(v);
	}
	
	public static IntegerHolder valueOf(Integer v) {
		return (v == null) ? NULL_HOLDER : valueOf(v.intValue());
	}
			
	public IntegerHolder(int value) {
		this.value = Integer.valueOf(value);
	}
	
	protected IntegerHolder() {		
	}
	
	@Override
	public Integer value() {
		return this.value;
	}

	@Override
	public IntegerType getType() {
		return IntegerType.TYPE;
	}

	@Override
	public int getSqlType() {
		return PrimitiveType.INTEGER;
	}
	
	public static class Factory
		implements HolderFactory<Integer, IntegerType, IntegerHolder> {

		@Override
		public IntegerHolder newHolder(Integer value) {
			return IntegerHolder.valueOf(value);
		}
	}
	
	/**
	 * Returns <code>this</code>
	 */	
	@Override
	public IntegerHolder asIntegerHolder() {
		return this;
	}
	
	@Override
	public IntegerHolder self() {
		return this;
	}

	public static IntegerHolder as(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asIntegerHolder();
	}

}
