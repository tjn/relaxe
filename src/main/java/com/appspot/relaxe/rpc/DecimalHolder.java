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
package com.appspot.relaxe.rpc;

import com.appspot.relaxe.types.DecimalType;
import com.appspot.relaxe.types.PrimitiveType;


public class DecimalHolder
	extends AbstractPrimitiveHolder<Decimal, DecimalType, DecimalHolder> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = -8533627613502905762L;
	/**
	 * 
	 */	
	private Decimal value;	
	public static final DecimalHolder NULL_HOLDER = new DecimalHolder();
	public static final DecimalHolder ZERO = new DecimalHolder(Decimal.valueOf(0));
	public static final DecimalHolder ONE = new DecimalHolder(Decimal.valueOf(1));
	
	public static DecimalHolder valueOf(long unscaled, int scale) {
		return new DecimalHolder(Decimal.valueOf(unscaled, scale));
	}
	
	public static DecimalHolder valueOf(Decimal v) {
		return (v == null) ? NULL_HOLDER : new DecimalHolder(v);
	}
	
	public DecimalHolder(Decimal value) {
		this.value = value;		
	}
	
	protected DecimalHolder() {		
	}
	
	@Override
	public Decimal value() {
		return this.value;
	}

	@Override
	public DecimalType getType() {
		return DecimalType.TYPE;
	}

	@Override
	public int getSqlType() {
		return PrimitiveType.DECIMAL;
	}

	@Override
	public DecimalHolder self() {
		return this;
	}
	
	@Override
	public DecimalHolder asDecimalHolder() {
		return this;
	}
	
	public static DecimalHolder of(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asDecimalHolder();
	}
}
