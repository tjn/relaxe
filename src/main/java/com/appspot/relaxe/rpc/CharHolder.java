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


import com.appspot.relaxe.types.CharType;
import com.appspot.relaxe.types.PrimitiveType;

public class CharHolder
	extends StringHolder<CharType, CharHolder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479889538730689743L;
	public static final CharHolder NULL_HOLDER = new CharHolder();
	public static final CharHolder EMPTY_HOLDER = new CharHolder("");
	
	/**
	 * TODO: should we have size?
	 */
		
	protected CharHolder() {
		super();
	}

	public CharHolder(String value) {
		super(value);
	}
	
	public static CharHolder valueOf(String s) {
		return 
			(s == null) ? NULL_HOLDER : 
			(s.equals("")) ? EMPTY_HOLDER :
			new CharHolder(s);
	}
	
	@Override
	public CharType getType() {
		return CharType.TYPE;
	}
	
	@Override
	public int getSqlType() {
		return PrimitiveType.CHAR;
	}
	
	@Override
	public CharHolder asCharHolder() {
		return this;
	}

	@Override
	public CharHolder self() {
		return this;
	}

	public static CharHolder of(PrimitiveHolder<?, ?, ?> holder) {
		return holder.asCharHolder();
	}
	
}
