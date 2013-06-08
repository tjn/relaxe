/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.paging.ElementListPage;
import com.appspot.relaxe.paging.Fetcher;
import com.appspot.relaxe.paging.Receiver;


public class ListFetcher<E extends Serializable>
	implements Fetcher<Void, ElementListPage<E>, Receiver<ElementListPage<E>>> {
	
	private List<E> content;
		
	public ListFetcher(List<E> content) {
		super();
		this.content = content;		
	}

	@Override
	public void fetch(Void queryTemplate, FetchOptions opts,
			Receiver<ElementListPage<E>> resultReceiver,
			Receiver<Throwable> errorReceiver) {
				
		int size = this.content.size();
		int o = (int) opts.getOffset();
		Integer count = opts.getCount();
		
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
