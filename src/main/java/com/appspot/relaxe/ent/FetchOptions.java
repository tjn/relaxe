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
package com.appspot.relaxe.ent;

import java.io.Serializable;

public class FetchOptions
	implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5894929494069252288L;
	private long offset;
	private Adjustment adjustment;
	private Integer count;
	private boolean fetchCardinality;
	
	enum Adjustment {
		NONE,
		AUTO
	}
	
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected FetchOptions() {
	}
	
	public FetchOptions(int count, long offset) {
		this(Integer.valueOf(count), offset);
	}
	
	/**
	 * Negative offset is interpreted as an offset counting from the end.
	 * 
	 * @param pageSize
	 * @param offset
	 */	
	public FetchOptions(Integer pageSize, long offset) {
		this(pageSize, offset, Adjustment.AUTO, true);
	}
	
	public FetchOptions(Integer count, long offset, Adjustment adjust, boolean cardinality) {
		super();
		this.offset = offset;
		this.count = count;
		this.adjustment = adjust;		
		this.fetchCardinality = cardinality;
	}
	
	public FetchOptions(long offset) {
		super();
		this.offset = offset;
		this.count = null;
		this.adjustment = Adjustment.NONE;
	}


	public long getOffset() {
		return this.offset;
	}
	
	public Adjustment getAdjustment() {
		return adjustment;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public boolean getCardinality() {
		return fetchCardinality;		
	}
}
