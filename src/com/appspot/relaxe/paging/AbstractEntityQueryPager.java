/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.util.Map;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryResult;
import com.appspot.relaxe.ent.EntityQueryTemplate;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.model.ValueModel;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


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
	extends DefaultPagerModel<QT, RP, EP, RF>
	implements EntityDataObjectPager<A, R, T, E, H, F, M, C, RP, QT, EP>
{	
	public AbstractEntityQueryPager(QT template, RF fetcher, int initialPageSize, Map<SimplePagerModel.Command, ValueModel<String>> nmm) {
		super(template, fetcher, initialPageSize, nmm);
	}
	
	
}
