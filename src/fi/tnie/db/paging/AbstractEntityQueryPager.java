/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.ent.EntityQueryTemplate;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.model.IntegerModel;
import fi.tnie.db.model.NotNullableIntegerModel;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class AbstractEntityQueryPager<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	QT extends EntityQueryTemplate<A, R, T, E, H, F, M, QT>,
	P extends EntityQueryPager<A, R, T, E, H, F, M, QT, P, C>,
	C
>
	extends AbstractPager<P, C>
	implements EntityQueryPager<A, R, T, E, H, F, M, QT, P, C>
{		
	private EntityFetcher<A, R, T, E, H, F, M, QT> fetcher;	
	private QT template;
			
	private IntegerModel pageSize;
	private EntityQueryResult<A, R, T, E, H, F, M, QT> result;
			
	public final class ResultReceiver implements
			Receiver<EntityQueryResult<A, R, T, E, H, F, M, QT>> {
		
		private C command;
		
		public ResultReceiver(C action) {
			super();
			this.command = action;
		}

		@Override
		public void receive(EntityQueryResult<A, R, T, E, H, F, M, QT> result) {
			received(result, getAction());
		}

		private C getAction() {
			return command;
		}
	}

	private QT getTemplate() {
		return template;
	}
	
	public AbstractEntityQueryPager(EntityQueryResult<A, R, T, E, H, F, M, QT> result, EntityFetcher<A, R, T, E, H, F, M, QT> fetcher) {
		this(result.getRequest().getTemplate(), fetcher);
		this.result = result;
	}
	
	public AbstractEntityQueryPager(QT template, EntityFetcher<A, R, T, E, H, F, M, QT> fetcher) {
		super();		
		this.template = template;
		this.fetcher = fetcher;		
	}
	
	@Override
	public IntegerModel getPageSize() {
		if (pageSize == null) {
			pageSize = new NotNullableIntegerModel(20);			
		}

		return pageSize;	
	}	
	


	protected void received(EntityQueryResult<A, R, T, E, H, F, M, QT> newResult, C a) {		
		if (newResult == null) {
			throw new NullPointerException("newResult");
		}
		
		this.result = newResult;
		fireEvent(new PagingEvent<P, C>(self(), a));
	}	 

	public EntityQueryResult<A, R, T, E, H, F, M, QT> getResult() {
		return result;
	}
	
	public abstract P self();
	
	protected EntityFetcher<A, R, T, E, H, F, M, QT> getFetcher() {
		return fetcher;
	}

	protected void fetch(C command, FetchOptions opts) {
		ResultReceiver rr = new ResultReceiver(command);		
		this.fetcher.fetch(getTemplate(), opts, rr);
	}
	
//	private static Logger logger() {
//		return DefaultLogger.getLogger();
//	}


	@Override
	public EntityQueryResult<A, R, T, E, H, F, M, QT> getCurrentPage() {
		return result;
	}

}
