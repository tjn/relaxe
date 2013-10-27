/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.paging;

import java.sql.Connection;
import java.util.EnumMap;
import java.util.Map;

import com.appspot.relaxe.AbstractUnitTest;
import com.appspot.relaxe.QueryExecutor;
import com.appspot.relaxe.SynchronousDataObjectFetcher;
import com.appspot.relaxe.ent.DataObject;
import com.appspot.relaxe.ent.DataObjectQueryResult;
import com.appspot.relaxe.env.Implementation;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.model.ValueModel;
import com.appspot.relaxe.paging.DefaultDataObjectPager;
import com.appspot.relaxe.paging.DefaultPagerModel;
import com.appspot.relaxe.paging.SimplePagerModel;
import com.appspot.relaxe.paging.SimplePagerModel.Command;
import com.appspot.relaxe.query.DataObjectQuery;
import com.appspot.relaxe.ui.action.Action;


public abstract class DefaultDataObjectPagerTest<I extends Implementation<I>> extends AbstractUnitTest<I> {
	
	public void testPager(BaseTable table) throws Exception {			
		Connection c = newConnection();
				
		QueryExecutor qx = new QueryExecutor(getPersistenceContext());
		
		SynchronousDataObjectFetcher f = new SynchronousDataObjectFetcher(qx, c);
		DataObjectQuery q = new DataObjectQuery(table);
		
		Map<Command, String> nm = new EnumMap<Command, String>(Command.class);
		
		nm.put(Command.FIRST, "<<<");
		nm.put(Command.PREVIOUS, "<<");
		nm.put(Command.CURRENT, "^");
		nm.put(Command.NEXT, ">>");
		nm.put(Command.LAST, ">>>");		
		
		Map<Command, ValueModel<String>> nmm = DefaultPagerModel.createNameModelMap(nm);		
							
		DefaultDataObjectPager p = new DefaultDataObjectPager(q.getQueryExpression(), f, 20, nmm);
		
		// table is expected to contain at least 40 rows.

		p.fetchFirst();
		DataObjectQueryResult<DataObject> cp = p.getCurrentPage();
		assertNotNull(cp);
		assertEquals(0, cp.getOffset());
		assertEquals(20, cp.getContent().size());
		
		Action a = p.getAction(SimplePagerModel.Command.NEXT);
		boolean e = a.execute();
		assertTrue(e);
		cp = p.getCurrentPage();
				
		assertNotNull(cp);
		assertEquals(20, cp.getOffset());
		assertEquals(20, cp.getContent().size());		
	}
	
	
	@Override
	protected abstract PersistenceContext<I> getPersistenceContext();
}
