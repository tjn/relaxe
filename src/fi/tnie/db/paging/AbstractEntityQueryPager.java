/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.util.Map;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityQueryPager<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>,
	RP extends EntityQueryResult<A, R, T, E, H, F, M, C, QT>,
	RF extends Fetcher<QT, RP, Receiver<RP>>,
	EP extends AbstractEntityQueryPager<A, R, T, E, H, F, M, C, QT, RP, RF, EP>
>
	extends DefaultPager<QT, RP, EP, RF>
	implements EntityDataObjectPager<A, R, T, E, H, F, M, C, RP, QT, EP>
{	
	public AbstractEntityQueryPager(QT template, RF fetcher, int initialPageSize, Map<SimplePager.Command, ValueModel<String>> nmm) {
		super(template, fetcher, initialPageSize, nmm);
	}
	
	
}
