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
import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.paging.ElementListPage;
import com.appspot.relaxe.paging.Fetcher;
import com.appspot.relaxe.paging.PageReceiver;


public class ListFetcher<E extends Serializable>
	implements Fetcher<Void, ElementListPage<E>, PageReceiver<ElementListPage<E>>> {
	
	private List<E> content;
		
	public ListFetcher(List<E> content) {
		super();
		this.content = content;		
	}

	@Override
	public void fetch(Void queryTemplate, FetchOptions opts,
			PageReceiver<ElementListPage<E>> resultReceiver,
			PageReceiver<Throwable> errorReceiver) {
				
		int size = this.content.size();
		int o = (int) opts.getOffset();
		Integer count = opts.getLimit();
		
		int c = (count == null) ? size - o : count.intValue();
		
		List<E> data = new ArrayList<E>(c);
		
		for (int i = 0; i < c; i++) {
			if (o + i < size) {
				data.add(this.content.get(o + i));
			}
			else {
				break;
			}
		}
	
		Long available = Long.valueOf(this.content.size());
		ElementListPage<E> p = new ElementListPage<E>(data, o, available, opts);
		resultReceiver.receive(p);
	}
}
