/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Properties;

import com.appspot.relaxe.TestContext;
import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.env.ConnectionManager;
import com.appspot.relaxe.env.DefaultConnectionManager;
import com.appspot.relaxe.env.DefaultDataAccessContext;
import com.appspot.relaxe.env.DriverManagerConnectionFactory;
import com.appspot.relaxe.env.PersistenceContext;
import com.appspot.relaxe.env.pg.PGImplementation;
import com.appspot.relaxe.gen.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pagila.ent.pub.Film.Attribute;
import com.appspot.relaxe.gen.pagila.ent.pub.Film.MetaData;
import com.appspot.relaxe.gen.pagila.ent.pub.Language;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.service.DataAccessContext;
import com.appspot.relaxe.service.DataAccessSession;
import com.appspot.relaxe.service.EntitySession;


public class PagilaDataAccessSessionTest 
	extends AbstractPagilaTestCase {

	public void testLoad() throws Exception {		
		logger().debug("testLoad - enter");
		
		DataAccessSession das = newSession();
		
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



	public void testLoad2() throws Exception {
		
		DataAccessSession das = newSession();		
		EntitySession es = das.asEntitySession();
		
		Film.QueryTemplate q = new Film.QueryTemplate();
		
		q.add(Film.TITLE);
		q.add(Film.DESCRIPTION);
						
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		
		BaseTable table = meta.getBaseTable();
		
		for (Film.Attribute a : meta.attributes()) {
			Column col = meta.getColumn(a);
			assertNotNull(col);
			Attribute b = meta.getAttribute(col);
			assertSame(a, b);
		}
		
		
		
		
				
//		assertEquals(meta.attributes(), q.getAttributeMap().keySet());
		
		List<Film> list = es.load(q, null);
			
		assertNotNull(list);
		assertTrue(list.size() > 10);			
		
		for (Film film : list) {
			assertNotNull(film);
			assertTrue(film.attributes().contains(Film.TITLE.name()));
			assertTrue(film.attributes().contains(Film.DESCRIPTION.name()));			
		}
	}

	
	public void testLoadAllAttributes() throws Exception {
		
		DataAccessSession das = newSession();
		
		EntitySession es = das.asEntitySession();
		
		Film.QueryTemplate q = new Film.QueryTemplate();
		q.addAllAttributes();
						
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		
//		assertEquals(meta.attributes(), q.getAttributeMap().keySet());
		
		List<Film> list = es.load(q, null);
		
		assertNotNull(list);
		assertTrue(list.size() > 10);			
		
		for (Film film : list) {
			assertNotNull(film);			
			assertEquals(meta.attributes(), film.attributes());			
		}
	}
		
}
