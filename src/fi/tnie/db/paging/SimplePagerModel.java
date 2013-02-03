/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

import fi.tnie.db.model.ImmutableValueModel;

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
