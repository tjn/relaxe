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
package com.appspot.relaxe.paging;

import java.io.Serializable;
import java.util.List;

import com.appspot.relaxe.ent.FetchOptions;


public class ElementListPage<E extends Serializable>
	implements Page<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1342370951301316614L;
	
	private List<E> content;
	private Long available;
	private long offset;
	private FetchOptions options;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected ElementListPage() {
	}
	
	public ElementListPage(List<E> content, long offset, Long available, FetchOptions options) {
		super();
		this.content = content;
		this.offset = offset;
		this.options = options;
		this.available = available;		
	}

	@Override
	public Long available() {
		return this.available;
	}

	@Override
	public List<E> getContent() {	
		return this.content;
	}

	@Override
	public long offset() {
		return this.offset;
	}

	@Override
	public FetchOptions options() {
		return this.options;
	}

}
