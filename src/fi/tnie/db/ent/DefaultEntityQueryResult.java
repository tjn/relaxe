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
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, C, QT>
>
	extends AbstractResponse<EntityQuery<A, R, T, E, H, F, M, C, QT>>
	implements EntityQueryResult<A, R, T, E, H, F, M, C, QT> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6159979642605451277L;
	
	private DataObjectQueryResult<EntityDataObject<E>> content;
		
	protected DefaultEntityQueryResult() {
		super();
	}

	public DefaultEntityQueryResult(EntityQuery<A, R, T, E, H, F, M, C, QT> request, DataObjectQueryResult<EntityDataObject<E>> content) {
		super(request);
		this.content = content;
		this.content.getContent().size();
				
	}

	public DataObjectQueryResult<EntityDataObject<E>> getContent() {
		return this.content;
	}
	
	@Override
	public FetchOptions getFetchOptions() {		
		return getContent().getFetchOptions();
	}
	
	@Override
	public int size() {		
		return getContent().size();
	}
	
	@Override
	public Boolean isLastPage() {		
		return getContent().isLastPage();
	}
	
	@Override
	public long getOffset() {		
		return getContent().getOffset();
	}
	
	@Override
	public Long available() {
		return getContent().available();
	}

	@Override
	public DataObjectQueryResult<EntityDataObject<E>> getResult() {
		return getContent();
	}	
}
