/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg.pagila;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Properties;

import fi.tnie.db.TestContext;
import fi.tnie.db.ent.FetchOptions;
import fi.tnie.db.env.DefaultConnectionManager;
import fi.tnie.db.env.DefaultDataAccessContext;
import fi.tnie.db.env.DriverManagerConnectionFactory;
import fi.tnie.db.env.PersistenceContext;
import fi.tnie.db.env.pg.PGImplementation;
import fi.tnie.db.gen.pagila.ent.pub.Film;
import fi.tnie.db.gen.pagila.ent.pub.Language;
import fi.tnie.db.service.DataAccessSession;
import fi.tnie.db.service.EntitySession;

public class PagilaDataAccessSessionTest 
	extends AbstractPagilaTestCase {

	public void testLoad() throws Exception {		
		logger().debug("testLoad - enter");
		
		// fi.tnie.db.gen.pagila.ent.LiteralCatalog.getInstance();
		
		TestContext<PGImplementation> tc = getContext();
				
		String jdbcURL = tc.getJdbcURL();
		Properties config = tc.getJdbcConfig();
		
		
		logger().debug("testLoad: jdbcURL=" + jdbcURL);
		logger().debug("testLoad: jdbcConfig=" + config);
		
		DriverManagerConnectionFactory cf = new DriverManagerConnectionFactory();
		DefaultConnectionManager cm = new DefaultConnectionManager(cf, jdbcURL, config);
		
		// TODO: make class abstract
		PersistenceContext<PGImplementation> pc = getPersistenceContext();

		DefaultDataAccessContext<PGImplementation> dctx = new DefaultDataAccessContext<PGImplementation>(pc, cm);
		
		DataAccessSession das = dctx.newSession();		
		assertNotNull(das);
		
		EntitySession es = das.asEntitySession();
		assertNotNull(es);	
						
		Film.QueryTemplate fq = new Film.QueryTemplate();
		fq.addAllAttributes();
		
		Language.QueryTemplate pt = new Language.QueryTemplate();
		pt.addAllAttributes();		
		fq.setTemplate(Film.LANGUAGE_ID_FKEY, pt);

		fq.setTemplate(Film.ORIGINAL_LANGUAGE_ID_FKEY, pt);
				
		FetchOptions fo = new FetchOptions(20, 0);
		List<Film> load = es.load(fq, fo);
		
		logger().debug("testLoad again: result set size: " + load.size());
		es.load(fq, fo);

		
		PagilaInspector inspector = new PagilaInspector();						
		inspector.inspect(load);
		
		logger().debug("testLoad: result set size: " + load.size());
		logger().debug("testLoad: stats:\n\n" + inspector);
				
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(data);
		
		oos.writeObject(load);
		oos.close();
				
		das.close();
		
		logger().debug("testLoad: data.size()=" + data.size());
		logger().debug("testLoad: inspector.getInstanceCount() : " + inspector.getInstanceCount());
		logger().debug("testLoad: inspector.getReferenceCount(): " + inspector.getReferenceCount());
		logger().debug("testLoad - exit");
	}
		
}
