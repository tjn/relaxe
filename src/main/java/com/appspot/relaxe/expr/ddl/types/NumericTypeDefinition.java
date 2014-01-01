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
package com.appspot.relaxe.expr.ddl.types;

public class NumericTypeDefinition
    extends FixedPrecisionDefinition {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5794913835887942942L;

	public NumericTypeDefinition() {
		super();
	}
	
	public NumericTypeDefinition(int precision, int scale) {
		this(Integer.valueOf(precision), Integer.valueOf(scale));
	}
	
	public NumericTypeDefinition(Integer precision, Integer scale) {
		super(precision, scale, SQLTypeDefinition.Name.NUMERIC);
	}	
	
	public NumericTypeDefinition(int precision) {
		super(precision);
	}
	
	public static NumericTypeDefinition get(int precision, int scale) {
		return new NumericTypeDefinition(precision, scale);
	}

	
	public static NumericTypeDefinition get(Integer precision, Integer scale) {
		return new NumericTypeDefinition(precision, scale);
	}


	@Override
	public Name getSQLTypeName() {
		return SQLTypeDefinition.Name.NUMERIC;
	}	
}
