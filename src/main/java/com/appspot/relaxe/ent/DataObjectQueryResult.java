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

import java.util.List;

import com.appspot.relaxe.paging.HasDataObjectQueryResult;
import com.appspot.relaxe.query.Query;
import com.appspot.relaxe.query.QueryResult;
import com.appspot.relaxe.query.QueryTime;


public class DataObjectQueryResult<T extends DataObject>
	extends QueryResult<T> implements HasDataObjectQueryResult<T>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4860198226914576207L;
	
	private DataObject.MetaData meta;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private DataObjectQueryResult() {
	}
	
	public DataObjectQueryResult(Query request, DataObject.MetaData meta, List<? extends T> content, QueryTime elapsed, FetchOptions options, long offset) {
		super(request, content, elapsed, options, offset);
		this.meta = meta;
	}
		
	public DataObject.MetaData getMeta() {
		return meta;
	}
	
	public T get(int index) {				
		return getContent().get(index);
	}
	
	@Override
	public com.appspot.relaxe.ent.DataObjectQueryResult<T> getResult() {
		return this;
	}
}
