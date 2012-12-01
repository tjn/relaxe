/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import fi.tnie.db.Inspector;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.env.DefaultConnectionManager;
import fi.tnie.db.env.DefaultDataAccessContext;
import fi.tnie.db.env.DriverManagerConnectionFactory;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.gen.pg.ent.LiteralCatalog;
import fi.tnie.db.gen.pg.ent.personal.HourReport;
import fi.tnie.db.gen.pg.ent.personal.Organization;
import fi.tnie.db.gen.pg.ent.personal.Project;
import fi.tnie.db.meta.impl.pg.PGTestCase;
import fi.tnie.db.service.DataAccessException;
import fi.tnie.db.service.DataAccessSession;
import fi.tnie.db.service.EntitySession;

public class DataAccessSessionTest 
	extends PGTestCase {


	public void testLoad() throws DataAccessException, EntityException, IllegalArgumentException, IllegalAccessException, IOException {
		
		// fi.tnie.db.gen.pg.ent.LiteralCatalog.getInstance();
		
		String jdbcURL = getDatabaseURL();
		
		logger().debug("testLoad: jdbcURL=" + jdbcURL);
		
		DriverManagerConnectionFactory cf = new DriverManagerConnectionFactory();
		DefaultConnectionManager cm = new DefaultConnectionManager(cf, jdbcURL, getJdbcConfig());
		DefaultDataAccessContext<Implementation> dctx = new DefaultDataAccessContext<Implementation>(getImplementation(), cm);
		
		DataAccessSession das = dctx.newSession();		
		assertNotNull(das);
		
		EntitySession es = das.asEntitySession();
		assertNotNull(es);
		
		HourReport.QueryTemplate qt = new HourReport.QueryTemplate();
		qt.addAllAttributes();
		
		Project.QueryTemplate pt = new Project.QueryTemplate();
		pt.addAllAttributes();		
		qt.setTemplate(HourReport.FK_HHR_PROJECT, pt);
				
		Organization.QueryTemplate ot = new Organization.QueryTemplate();
		ot.addAllAttributes();		
		pt.setTemplate(Project.FK_CLIENT, ot);
				
		FetchOptions fo = new FetchOptions(20, 0);
		List<HourReport> load = es.load(qt, fo);
		
		logger().debug("testLoad again: result set size: " + load.size());
		es.load(qt, fo);

		
		Inspector inspector = new Inspector();						
		inspector.inspect(load);
		
		logger().debug("testLoad: result set size: " + load.size());
		logger().debug("testLoad: stats:\n\n" + inspector);
				
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(data);
		
		oos.writeObject(load);
		oos.close();
				
		das.close();
		
		logger().debug("testLoad: data.size()=" + data.size());
				
//		logger().debug("testLoad: inspector.getInstanceCount() : " + inspector.getInstanceCount());
//		logger().debug("testLoad: inspector.getReferenceCount(): " + inspector.getReferenceCount());
		
		
		
		
		
	}
	

	
	
	
	
}
