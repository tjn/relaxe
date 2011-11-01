/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public interface EntityQueryPager<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	RP extends EntityQueryResult<A, R, T, E, H, F, M, QT>,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, QT>,
	P extends EntityQueryPager<A, R, T, E, H, F, M, RP, QT, P>
> 
	extends DataObjectPager<EntityDataObject<E>, RP, P>
{
	void fetchFirst();	
}
