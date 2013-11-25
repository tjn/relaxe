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

import java.util.List;


public class StringArray
	extends AbstractArray<String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2522909517688093544L;
	private static final String[] EMPTY = {};

	public StringArray() {
		super(EMPTY);
	}
	
	public StringArray(StringArray src) {
		super(src.getContent());
	}
	
	public StringArray(ArrayValue<String> src) {
		super(src);
	}

	public StringArray(String[] content) {
		super(copy(content));
	}
	
	public StringArray(List<String> content) {
		super(copy(content.toArray(EMPTY)));
	}
	
	@Override
	public String[] toArray() {		
		return copy(this.getContent());
	}	
	
	public static String[] copy(String[] src) {
		String[] dest = new String[src.length];
		System.arraycopy(src, 0, dest, 0, src.length);
		return dest;
	}		
}
