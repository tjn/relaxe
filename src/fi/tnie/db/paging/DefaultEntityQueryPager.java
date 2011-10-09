/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.util.Map;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class DefaultEntityQueryPager<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, QT>
> 
	extends AbstractEntityQueryPager<A, R, T, E, H, F, M, QT, 
		EntityQueryResult<A, R, T, E, H, F, M, QT>, 
		Fetcher<QT, EntityQueryResult<A, R, T, E, H, F, M, QT>, Receiver<EntityQueryResult<A, R, T, E, H, F, M, QT>>>, 
		DefaultEntityQueryPager<A,R,T,E,H,F,M,QT>> {
	
	
	public DefaultEntityQueryPager(
			QT template, 
			Fetcher<QT, EntityQueryResult<A, R, T, E, H, F, M, QT>, Receiver<EntityQueryResult<A, R, T, E, H, F, M, QT>>> fetcher, 
			Map<SimplePager.Command, ValueModel<String>> nmm) {
		this(template, fetcher, 20, nmm);
	}
	
	public DefaultEntityQueryPager(QT template, Fetcher<QT, EntityQueryResult<A, R, T, E, H, F, M, QT>, Receiver<EntityQueryResult<A, R, T, E, H, F, M, QT>>> fetcher, 
			Map<SimplePager.Command, String> nm, int initialPageSize) {
		super(template, fetcher, initialPageSize, createNameModelMap(nm));
	}
	
	public DefaultEntityQueryPager(QT template, Fetcher<QT, EntityQueryResult<A, R, T, E, H, F, M, QT>, Receiver<EntityQueryResult<A, R, T, E, H, F, M, QT>>> fetcher, 
			int initialPageSize, Map<SimplePager.Command, ValueModel<String>> nmm) {
		super(template, fetcher, initialPageSize, nmm);
	}

	@Override
	public DefaultEntityQueryPager<A, R, T, E, H, F, M, QT> self() {
		return this;
	}	
}
