/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

public class PagingEvent<T extends Serializable, P extends Pager<T, P, X>, X> {
	
	private P pager;
	private X action;
			
	public PagingEvent(P pager, X action) {
		super();
		this.pager = pager;
		this.action = action;
	}
	
	public P getPager() {
		return this.pager;
	}
	
	public X getAction() {
		return this.action;
	}
}
