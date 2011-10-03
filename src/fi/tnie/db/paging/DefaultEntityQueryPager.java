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
import fi.tnie.db.ent.Reference;
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
		DefaultEntityQueryPager<A, R, T, E, H, F, M, QT>, 
		DefaultEntityQueryPager.Command>	
{
	public DefaultEntityQueryPager(EntityQueryResult<A, R, T, E, H, F, M, QT> result, EntityFetcher<A, R, T, E, H, F, M, QT> fetcher) {
		super(result, fetcher);
	}

	public enum Command {
		FIRST,
		PREVIOUS,
		NEXT
	}

	@Override
	public DefaultEntityQueryPager<A, R, T, E, H, F, M, QT> self() {
		return this;
	}
	

	@Override
	public void run(Command command) {
		if (command == null) {
			throw new NullPointerException("command");
		}
		
		switch (command) {
		case FIRST:
			fetch(Command.FIRST, null);
		case NEXT:
			fetchNext();
		case PREVIOUS:
			fetchPrevious();
			break;		
		default:
			break;
		}		
	}

	private void fetch(Command action, Long offset) {				
		Long limit = getLimit();				
		super.fetch(limit, offset, action);
	}

	private void fetchNext() {
		long co = getCurrentOffset();
		int ps = getPageSize().get().intValue();			
		Long offset = Long.valueOf(co + ps);
		fetch(Command.NEXT, offset);
	}
	
	private void fetchPrevious() {
		long co = getCurrentOffset();
		
		if (co == 0) {
			return;
		}
		
		int ps = getPageSize().get().intValue();
		long pos = co - ps;						
		Long offset = Long.valueOf(pos < 0 ? 0 : pos);
		fetch(Command.PREVIOUS, offset);		
	}

	private Long getLimit() {
		Integer ps = getPageSize().get();
		Long limit = Long.valueOf(ps.intValue());
		return limit;
	}

	private long getCurrentOffset() {
		Long off = getResult().getRequest().getOffset();
		return (off == null) ? 0 : off.longValue();
	}

}
