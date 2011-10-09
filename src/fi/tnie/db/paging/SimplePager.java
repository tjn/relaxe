/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.io.Serializable;

public interface SimplePager<T extends Serializable, P extends Pager<T, P, SimplePager.Command>>
	extends Pager<T, P, SimplePager.Command> {
	
	enum Command {		
		FIRST,
		PREVIOUS,
		CURRENT,
		NEXT,
		LAST
	}
	
	public int getPageSize();
}
