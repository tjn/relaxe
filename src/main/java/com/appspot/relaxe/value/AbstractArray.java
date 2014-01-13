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
package com.appspot.relaxe.value;

import java.io.Serializable;

public abstract class AbstractArray<E extends Serializable>
	implements Serializable, ArrayValue<E> {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4658618167635030919L;
	
	private E[] content = null;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private AbstractArray() {
	}
	
	protected E[] getContent() {
		return content;
	}
	
	public AbstractArray(ArrayValue<E> src) {
		if (src == null) {
			throw new NullPointerException("src");
		}
		
		this.content = src.toArray();
	}
		
	protected AbstractArray(E[] content) {
		if (content == null) {
			throw new NullPointerException("content");
		}
		
		this.content = content; 
	}

	/* (non-Javadoc)
	 * @see com.appspot.relaxe.rpc.ArrayValue#size()
	 */
	@Override
	public int size() {
		return this.content.length;
	}
	
	/* (non-Javadoc)
	 * @see com.appspot.relaxe.rpc.ArrayValue#get(int)
	 */
	@Override
	public E get(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		
		return this.content[index];
	}

	@Override
	public abstract E[] toArray();
}
