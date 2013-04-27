/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.util.EnumSet;

public class PagerEvent<
	G extends Pager<?, ?, ?, G>	
> {		
	private G source;
	private EnumSet<Pager.Flags> flags;
	
	public PagerEvent(G source) {
		this.source = source;		
	}
	
	public PagerEvent(G source, Pager.Flags first) {
		this(source);		
		this.flags = EnumSet.of(first);
	}
		
	public PagerEvent(G source, Pager.Flags first, Pager.Flags ... rest) {
		this(source);		
		this.flags = EnumSet.of(first, rest);
	}

	public G getSource() {
		return source;
	}
		
	public boolean on(Pager.Flags flag) {
		return flags != null && flags.contains(flag);
	}
	
}
