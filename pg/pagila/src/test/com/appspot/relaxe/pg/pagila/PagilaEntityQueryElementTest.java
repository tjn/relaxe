/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.gen.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pagila.ent.pub.Language;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;

public class PagilaEntityQueryElementTest 
	extends AbstractPagilaTestCase {
	
	public void testAddAllAttributes() throws Exception {
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		assertFalse(meta.attributes().isEmpty());
		Film.QueryElement.Builder qb = new Film.QueryElement.Builder();				
		qb.addAllAttributes();
		
		Film.QueryElement qe = qb.newQueryElement();		
		assertFalse(qe.attributes().isEmpty());
		assertEquals(meta.attributes(), qe.attributes());
	}
	
	public void testQueryElements() throws Exception {				
		Film.QueryElement.Builder qb = new Film.QueryElement.Builder();
		
		Language.QueryElement.Builder lb = new Language.QueryElement.Builder();
		final Language.QueryElement le = lb.newQueryElement();
				
		qb.setQueryElement(Film.LANGUAGE, le);
		assertNotNull(qb.getQueryElement(Film.LANGUAGE));
		assertNull(qb.getQueryElement(Film.ORIGINAL_LANGUAGE));
		
		final EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> rle = qb.getQueryElement(Film.LANGUAGE);
		assertNotNull(rle);		
		assertTrue(rle == le);
		
		final Language.QueryElement ole = lb.newQueryElement();
		
		qb.setQueryElement(Film.ORIGINAL_LANGUAGE, ole);
		EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> r2 = qb.getQueryElement(Film.ORIGINAL_LANGUAGE);
		assertNotNull(r2);
		assertTrue(r2 == ole);
		
		Film.QueryElement fqe = qb.newQueryElement();
		
		EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> s1 = fqe.getQueryElement(Film.LANGUAGE);
		assertNotNull(s1);
		assertTrue(s1 == rle);
		
		EntityQueryElement<?, ?, ?, ?, ?, ?, ?, ?, ?> s2 = fqe.getQueryElement(Film.ORIGINAL_LANGUAGE);
		assertNotNull(s2);
		assertTrue(s2 == ole);
		
		
	}
}
