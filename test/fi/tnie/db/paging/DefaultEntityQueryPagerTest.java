/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.paging;

import java.sql.Connection;
import java.sql.SQLException;
import fi.tnie.db.AbstractUnitTest;
import fi.tnie.db.HourReportFetcher;
import fi.tnie.db.HourReportPager;
import fi.tnie.db.ent.EntityQueryResult;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.gen.ent.LiteralCatalog;
import fi.tnie.db.gen.ent.personal.HourReport;
import fi.tnie.db.gen.ent.personal.HourReport.Attribute;
import fi.tnie.db.gen.ent.personal.HourReport.Factory;
import fi.tnie.db.gen.ent.personal.HourReport.Holder;
import fi.tnie.db.gen.ent.personal.HourReport.MetaData;
import fi.tnie.db.gen.ent.personal.HourReport.QueryTemplate;
import fi.tnie.db.gen.ent.personal.HourReport.Reference;
import fi.tnie.db.gen.ent.personal.HourReport.Type;
import fi.tnie.db.paging.SimplePager.Command;
import fi.tnie.db.ui.action.Action;

public class DefaultEntityQueryPagerTest extends AbstractUnitTest {

	public void testConstructor() throws SQLException {
		LiteralCatalog.getInstance();
		
		Implementation imp = getImplementation();
		Connection c = getContext().newConnection();
		
		HourReportFetcher hf = new HourReportFetcher(imp, c);
		HourReport.QueryTemplate qt = new HourReport.QueryTemplate();
		
		HourReportPager p = new HourReportPager(qt, hf);
		
		SimplePager.Command.values();
		
		for (Command cmd : SimplePager.Command.values()) {
			logger().debug("testConstructor: cmd=" + cmd);
			Action a = p.getAction(cmd);
			assertNotNull(a);
		}
		EntityQueryResult<Attribute, Reference, Type, HourReport, Holder, Factory, MetaData, QueryTemplate> cp = null; 
						
		cp = p.getCurrentPage();
		assertNull(cp);
		
		Action f = p.getAction(Command.FIRST);
		Action n = p.getAction(Command.NEXT);
		assertNotNull(f.enablement());
		
		p.fetchFirst();
		
		cp = p.getCurrentPage();
		assertNotNull(cp);
		
		Long a = cp.available();
		logger().debug("testConstructor: available=" + a);
		assertNotNull(a);
		
		int ps = cp.size();
		logger().debug("testConstructor: ps=" + ps);		
						
		assertFalse(p.hasPreviousPage().get().booleanValue());
		assertTrue(p.hasNextPage().get().booleanValue());
		
		assertFalse(f.enablement().get().booleanValue());
		assertTrue(n.enablement().get().booleanValue());
		
		
		c.close();
		
	}
}
