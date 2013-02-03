/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

public interface PagerEventHandler<
	G extends Pager<?, ?, ?, G>
	
> {		 
	void handleEvent(PagerEvent<G> e);		
}
