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

import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.paging.ResultPage;
import com.appspot.relaxe.rpc.AbstractResponse;



public class QueryResult<T>
	extends AbstractResponse<Query>
	implements ResultPage {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3297095270132748103L;
	
	protected QueryResult() {
		super();
	}
	
	private Long available;
	private List<? extends T> content;
	private QueryTime elapsed;
	private FetchOptions options;
	private long offset;
			
	public QueryResult(Query request, List<? extends T> content, QueryTime elapsed, FetchOptions options, long offset) {
		this(request, content);
		this.elapsed = elapsed;		
		this.options = options;
		this.offset = offset;
	}
	
	public QueryResult(Query request, List<? extends T> content) {
		super(request);
		this.content = content;
	}	

	public List<? extends T> getContent() {
		if (content == null) {
			content = new ArrayList<T>();			
		}

		return content;
	}

	public Long getAvailable() {
		return available;
	}

	public void setAvailable(Long available) {
		this.available = available;
	}

	public QueryTime getElapsed() {
		return elapsed;
	}
	
	@Override
	public FetchOptions getFetchOptions() {
		return this.options;
	}

	@Override
	public long getOffset() {
		return offset;
	}
		
	@Override
	public int size() {		
		return (this.content == null) ? 0 : this.content.size();
	}	

	@Override
	public Boolean isLastPage() {		
		Long a = this.available;
		
		if (a == null) {
			return null;
		}
			
		boolean more = a.longValue() > getOffset() + size();
		return Boolean.valueOf(!more);
	}
	
	
	@Override
	public Long available() {
		return getAvailable();
	}
}
