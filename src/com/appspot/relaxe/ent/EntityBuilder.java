/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import com.appspot.relaxe.rpc.ReferenceHolder;

public interface EntityBuilder<
	E extends Entity<?, ?, ?, E, H, ?, ?, ?>,
	H extends ReferenceHolder<?, ?, ?, E, H, ?, ?>
> {	
	
	/**
	 * Attempts
	 * @param src
	 * @return
	 */
	H read(DataObject src);
}
