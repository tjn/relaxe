/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

public class PagingEvent<P extends Pager<P, X>, X> {
	
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
