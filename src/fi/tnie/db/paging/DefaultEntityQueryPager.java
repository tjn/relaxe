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
	
	public DefaultEntityQueryPager(QT template, EntityFetcher<A, R, T, E, H, F, M, QT> fetcher) {
		super(template, fetcher);	
	}

	public enum Command {
		CURRENT_PAGE,
		FIRST_PAGE,
		PREVIOUS_PAGE,
		NEXT_PAGE,
		LAST_PAGE,
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
		case FIRST_PAGE:
			fetchFirst();
			break;
		case CURRENT_PAGE:
			fetchCurrent();
			break;
		case NEXT_PAGE:
			fetchNext();
			break;
		case PREVIOUS_PAGE:
			fetchPrevious();
			break;
		case LAST_PAGE:
			fetchLast();
			break;			
		default:
			break;
		}		
	}

//	private void fetch(Command action, FetchOptions opts) {				
//		Long limit = getLimit();				
//		super.fetch(limit, opts, action);
//	}

	public void fetchFirst() {		 
		int ps = getPageSize().get().intValue();
		FetchOptions fo = new FetchOptions(ps, 0);		
		fetch(Command.FIRST_PAGE, fo);		
	}
	

	public void fetchCurrent() {		 
		int ps = getPageSize().get().intValue();
		Long o = getCurrentOffset();
		long off = (o == null) ? 0 : o.longValue(); 
		FetchOptions fo = new FetchOptions(ps, off);		
		fetch(Command.CURRENT_PAGE, fo);
	}
	
	public void fetchNext() {	
		long co = getCurrentOffset().longValue();
		int ps = getPageSize().get().intValue();
		FetchOptions fo = new FetchOptions(ps, co + ps);		
		fetch(Command.NEXT_PAGE, fo);		
	}
	
	
	public void fetchPrevious() {
		long co = getCurrentOffset().longValue();
		
		if (co == 0) {
			return;
		}
		
		int ps = getPageSize().get().intValue();
		long pos = co - ps;
		long off = pos < 0 ? 0 : pos;
				
		FetchOptions opts = new FetchOptions(ps, off);		
		fetch(Command.PREVIOUS_PAGE, opts);		
	}
	
	public void fetchLast() {
		int ps = getPageSize().get().intValue();		  
		FetchOptions fo = new FetchOptions(ps, -ps);				
		fetch(Command.LAST_PAGE, fo);		
	}	

//	private Long getLimit() {
//		Integer ps = getPageSize().get();
//		Long limit = Long.valueOf(ps.intValue());
//		return limit;
//	}

	private Long getCurrentOffset() {
		Long off = null;
						
		if (getResult() != null) {			
			long offset = getResult().getContent().getOffset();
			off = Long.valueOf(offset);
		}		

		return (off == null) ? Long.valueOf(0) : off;
	}
	
	
}
