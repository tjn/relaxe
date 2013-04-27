/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.paging.ElementListPage;
import com.appspot.relaxe.paging.Fetcher;
import com.appspot.relaxe.paging.Receiver;


public class StringListFetcher
	implements Fetcher<Void, ElementListPage<String>, Receiver<ElementListPage<String>>> {
	
	private List<String> content;
	private Long available = null;

	public StringListFetcher(List<String> content) {
		super();
		this.content = content;
		this.available = Long.valueOf(this.content.size());
	}



	@Override
	public void fetch(Void queryTemplate, FetchOptions opts,
			Receiver<ElementListPage<String>> resultReceiver,
			Receiver<Throwable> errorReceiver) {
				
		int size = this.content.size();
		int o = (int) opts.getOffset();
		Integer count = opts.getCount();
		
		int c = (count == null) ? size - o : count.intValue();
		
		List<String> data = new ArrayList<String>(c);
		
		for (int i = 0; i < c; i++) {
			if (o + i < size) {
				data.add(this.content.get(o + i));
			}
			else {
				break;
			}
		}
		
		ElementListPage<String> p = new ElementListPage<String>(data, o, this.available, opts);
		resultReceiver.receive(p);
	}
}
