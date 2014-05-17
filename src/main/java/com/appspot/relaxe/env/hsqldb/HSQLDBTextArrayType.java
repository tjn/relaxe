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
package com.appspot.relaxe.env.hsqldb;

import com.appspot.relaxe.types.ArrayType;
import com.appspot.relaxe.types.VarcharType;

public class HSQLDBTextArrayType
	extends ArrayType<HSQLDBTextArrayType, VarcharType> {

	
	public static final HSQLDBTextArrayType TYPE = new HSQLDBTextArrayType();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7152862866657378339L;

	@Override
	public VarcharType getElementType() {
		return VarcharType.TYPE;
	}
	
	@Override
	public String getName() {
		return "()";
	}

	@Override
	public HSQLDBTextArrayType self() {
		return this;
	}
	
	@Override
	public ArrayType<? extends HSQLDBTextArrayType, ?> asArrayType() {
		return this;
	}
}
