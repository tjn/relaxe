/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.rpc.ReferenceHolder;

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
