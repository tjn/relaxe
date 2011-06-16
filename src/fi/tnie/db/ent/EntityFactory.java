/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.rpc.ReferenceHolder;

public interface EntityFactory<
	E extends Entity<?, ?, ?, E, H, F, M>,
	H extends ReferenceHolder<?, ?, ?, E, H, M>,
	M extends EntityMetaData<?, ?, ?, E, H, F, M>,
	F extends EntityFactory<E, H, M, F>

> {
	E newInstance();
	H newHolder(E value);
	M getMetaData();	
	F self();
}
