/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import fi.tnie.db.rpc.AbstractResponse;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class DefaultEntityQueryResult<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, QT>
>
	extends AbstractResponse<EntityQuery<A, R, T, E, H, F, M, QT>>
	implements EntityQueryResult<A, R, T, E, H, F, M, QT> {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6159979642605451277L;
	
	private DataObjectQueryResult<EntityDataObject<E>> content;

	protected DefaultEntityQueryResult() {
		super();
	}

	public DefaultEntityQueryResult(EntityQuery<A, R, T, E, H, F, M, QT> request, DataObjectQueryResult<EntityDataObject<E>> content) {
		super(request);
		this.content = content;
	}

	public DataObjectQueryResult<EntityDataObject<E>> getContent() {
		return this.content;
	}
}
