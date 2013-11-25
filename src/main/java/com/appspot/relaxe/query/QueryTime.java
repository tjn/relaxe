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
package com.appspot.relaxe.query;

import java.io.Serializable;

public class QueryTime
	implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7899684211701751541L;
	private long executionTime;
	private long populationTime;
	
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private QueryTime() {
	}
	
	public QueryTime(long executionTime) {
		super();
		this.executionTime = executionTime;
	}

	public QueryTime(long executionTime, long populationTime) {
		super();
		this.executionTime = executionTime;
		this.populationTime = populationTime;
	}
	
	public long getExecutionTime() {
		return executionTime;
	}
	
	public long getPopulationTime() {
		return populationTime;
	}
	
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		
		buf.append(super.toString());
		buf.append(": execution=");
		buf.append(this.executionTime);
		buf.append("ms; population: ");
		buf.append(this.populationTime);
		buf.append("ms");		
		
		return buf.toString();
	}
}
