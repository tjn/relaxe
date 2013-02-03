/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

public class PagerModelEvent<T extends Serializable, P extends PagerModel<T, P, X>, X> {
	
	private P pager;
	private X action;
			
	public PagerModelEvent(P pager, X action) {
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
