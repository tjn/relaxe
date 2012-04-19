/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.rpc.ReferenceHolder;

public interface EntityFactory<
	E extends Entity<?, ?, ?, E, H, F, M, C>,
	H extends ReferenceHolder<?, ?, ?, E, H, M, C>,
	M extends EntityMetaData<?, ?, ?, E, H, F, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	C extends Content
> {
	E newInstance();
	H newHolder(E value);
	M getMetaData();	
	F self();
}
