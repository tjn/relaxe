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
package com.appspot.relaxe.env.pg;

import com.appspot.relaxe.rpc.PrimitiveHolder;
import com.appspot.relaxe.rpc.StringHolder;

public class PGTextHolder
	extends StringHolder<PGTextType, PGTextHolder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2479889538730689743L;
	public static final PGTextHolder NULL_HOLDER = new PGTextHolder();
	public static final PGTextHolder EMPTY_HOLDER = new PGTextHolder("");
	
	/**
	 * TODO: should we have size?
	 */
		
	protected PGTextHolder() {
		super();
	}

	public PGTextHolder(String value) {
		super(value);
	}
	
	public static PGTextHolder valueOf(String s) {
		return 
			(s == null) ? NULL_HOLDER : 
			(s.equals("")) ? EMPTY_HOLDER :
			new PGTextHolder(s);
	}
	
	public static String toString(PGTextHolder h) {
		return (h == null) ? null : h.value();
	}
	
	@Override
	public PGTextType getType() {
		return PGTextType.TYPE;
	}
	
	@Override
	public PGTextHolder asStringHolder() {		
		return this;
	}

	@Override
	public PGTextHolder self() {
		return this;
	}
	
	public static PGTextHolder of(PrimitiveHolder<?, ?, ?> holder) {
		Object s = holder.self();
				
		PGTextHolder h = (PGTextHolder) s;	
		return h;
	}
}
