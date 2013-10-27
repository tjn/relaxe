/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.appspot.relaxe.ent.FetchOptions;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film.Attribute;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
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
						
		Film.QueryElement.Builder fq = new Film.QueryElement.Builder();
		fq.addAllAttributes();
		
		Language.QueryElement.Builder pt = new Language.QueryElement.Builder();
		pt.addAllAttributes();
		
		Language.QueryElement le = pt.newQueryElement();
		fq.setQueryElement(Film.LANGUAGE, le);

		fq.setQueryElement(Film.ORIGINAL_LANGUAGE, le);
		
		Film.Query qo = new Film.Query(fq.newQueryElement());
				
		FetchOptions fo = new FetchOptions(20, 0);
		List<Film> load = es.load(qo, fo);
		
		logger().debug("testLoad again: result set size: " + load.size());
		es.load(qo, fo);

		
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
				
		Film.QueryElement.Builder qeb = new Film.QueryElement.Builder();
		
		qeb.add(Film.TITLE);
		qeb.add(Film.DESCRIPTION);
								
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		
		for (Film.Attribute a : meta.attributes()) {
			Column col = meta.getColumn(a);
			assertNotNull(col);
			Attribute b = meta.getAttribute(col);
			assertSame(a, b);
		}		
		
		{
			Film.Query qo = new Film.Query(qeb.newQueryElement());
			
			List<Film> list = es.load(qo, null);
				
			assertNotNull(list);
			assertTrue(list.size() > 10);			
			
			for (Film film : list) {
				assertNotNull(film);
				// ID by default builder
				assertTrue(film.attributes().contains(Film.FILM_ID.name()));
				assertTrue(film.attributes().contains(Film.TITLE.name()));
				assertTrue(film.attributes().contains(Film.DESCRIPTION.name()));			
			}
		}
		
		{
			qeb.remove(Film.Attribute.FILM_ID);
			
//			Film.Query qo = new Film.Query(qeb.newQueryElement());
//			
//			List<Film> list = es.load(qo, null);
//				
//			assertNotNull(list);
//			assertTrue(list.size() > 10);			
//			
//			for (Film film : list) {
//				assertNotNull(film);
//				// ID removed
//				assertFalse(film.attributes().contains(Film.FILM_ID.name()));
//				assertTrue(film.attributes().contains(Film.TITLE.name()));
//				assertTrue(film.attributes().contains(Film.DESCRIPTION.name()));			
//			}
		}
	}

	
	public void testLoadAllAttributes() throws Exception {
		
		DataAccessSession das = newSession();
		
		EntitySession es = das.asEntitySession();
		
		Film.QueryElement.Builder qeb = new Film.QueryElement.Builder();
		qeb.addAllAttributes();
								
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		
		
		Film.Query q = new Film.Query(qeb.newQueryElement());
						
		List<Film> list = es.load(q, null);
		
		assertNotNull(list);
		assertTrue(list.size() > 10);			
		
		for (Film film : list) {			
			assertNotNull(film);
			logger().debug("film: " + film.getContent().getFilmId());
			logger().debug("film.attributes(): " + film.attributes());
			logger().debug("meta.attributes(): " + meta.attributes());
			
			assertEquals(meta.attributes(), film.attributes());
		}
	}
		
}
