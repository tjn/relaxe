/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.Map;

import fi.tnie.db.AbstractUnitTest;
import fi.tnie.db.QueryExecutor;
import fi.tnie.db.SynchronousDataObjectFetcher;
import fi.tnie.db.ent.DataObject;
import fi.tnie.db.ent.DataObjectQueryResult;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.gen.pg.ent.LiteralCatalog;
import fi.tnie.db.gen.pg.ent.LiteralCatalog.LiteralBaseTable;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.paging.SimplePagerModel.Command;
import fi.tnie.db.query.DataObjectQuery;
import fi.tnie.db.ui.action.Action;

public abstract class DefaultDataObjectPagerTest<I extends Implementation<I>> extends AbstractUnitTest<I> {
	
	public void testPager() throws SQLException, ClassNotFoundException {
		
		LiteralCatalog.getInstance();
		
		Connection c = newConnection();
		
		
		QueryExecutor qx = new QueryExecutor(getPersistenceContext());
		
		SynchronousDataObjectFetcher f = new SynchronousDataObjectFetcher(qx, c);
		DataObjectQuery q = new DataObjectQuery(LiteralBaseTable.PUBLIC_CITY);
		
		Map<Command, String> nm = new EnumMap<Command, String>(Command.class);
		
		nm.put(Command.FIRST, "<<<");
		nm.put(Command.PREVIOUS, "<<");
		nm.put(Command.CURRENT, "^");
		nm.put(Command.NEXT, ">>");
		nm.put(Command.LAST, ">>>");		
		
		Map<Command, ValueModel<String>> nmm = DefaultPagerModel.createNameModelMap(nm);		
							
		DefaultDataObjectPager p = new DefaultDataObjectPager(q, f, 20, nmm);
		
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
