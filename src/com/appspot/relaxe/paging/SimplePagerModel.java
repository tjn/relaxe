/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.io.Serializable;

import com.appspot.relaxe.model.ImmutableValueModel;


public interface SimplePagerModel<T extends Serializable, P extends PagerModel<T, P, SimplePagerModel.Command>>
	extends PagerModel<T, P, SimplePagerModel.Command> {
	
	enum Command {		
		FIRST,
		PREVIOUS,
		CURRENT,
		NEXT,
		LAST
	}
	
	int getPageSize();
	
	ImmutableValueModel<Long> available();
	ImmutableValueModel<Long> currentPageOffset();	

}
