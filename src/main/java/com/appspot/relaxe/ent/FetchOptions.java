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
package com.appspot.relaxe.ent;

import java.io.Serializable;

public class FetchOptions
	implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1881575370251414370L;
	
	private long offset;	
	private Integer count;
	private boolean cardinality;
	private OffsetUnit offsetUnit;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected FetchOptions() {
	}
	
	public FetchOptions(int limit, long offset) {
		this(Integer.valueOf(limit), offset);
	}
	
	/**
	 * Negative offset is interpreted as an offset counting from the end.
	 * 
	 * @param limit
	 * @param offset
	 */	
	public FetchOptions(Integer limit, long offset) {
		this(limit, offset, null, true);
	}
	
	public FetchOptions(int count, boolean cardinality) {
		this(Integer.valueOf(count), 0, null, cardinality);
	}
	
	public FetchOptions(Integer count, long offset, OffsetUnit ou) {
		this(count, offset, ou, true);
	}
	
	private FetchOptions(Integer count, long offset, OffsetUnit ou, boolean cardinality) {
		super();		
		this.offsetUnit = (ou == null) ? OffsetUnit.ELEMENT : ou;
		this.offset = offset;
		this.count = count;				
		this.cardinality = cardinality;
	}

	public long getOffset() {
		return this.offset;
	}
		
	public Integer getLimit() {
		return count;
	}
	
	public boolean isCardinality() {
		return cardinality;		
	}
	
	public OffsetUnit getOffsetUnit() {
		return offsetUnit;
	}
}
