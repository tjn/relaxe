package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.rpc.IntegerHolder;

public class PagilaEntityAttributeTest
	extends AbstractPagilaTestCase {
		
	public void testAttributes() {		
		Film fo = Film.Type.TYPE.getMetaData().getFactory().newEntity();
		
		{
			assertNull(fo.getFilmId());
			Integer value = Integer.valueOf(13);		
			fo.setFilmId(value);
			assertNotNull(fo.getFilmId());
			assertNotNull(fo.getFilmId().value());
			assertEquals(value, fo.getFilmId().value());
			
			IntegerHolder h = fo.getInteger(Film.FILM_ID);
			assertNotNull(h);
			assertEquals(value, h.value());
		}
		
		{
			Integer value = Integer.valueOf(5);
			fo.setInteger(Film.FILM_ID, IntegerHolder.valueOf(value));
			assertEquals(value, fo.getFilmId().value());
		}
		
		{			
			fo.setInteger(Film.FILM_ID, IntegerHolder.valueOf(null));
			assertNotNull(fo.getFilmId());
			assertTrue(fo.getFilmId().isNull());
			assertTrue(fo.getFilmId().value() == null);
		}
		
		{			
			fo.setInteger(Film.FILM_ID, IntegerHolder.NULL_HOLDER);
			assertNotNull(fo.getFilmId());
			assertTrue(fo.getFilmId().isNull());
			assertTrue(fo.getFilmId().value() == null);
		}
		
		{
			fo.setFilmId((Integer) null);
			assertNotNull(fo.getInteger(Film.FILM_ID));
			assertNull(fo.getInteger(Film.FILM_ID).value());
		}
		
		{
			fo.setFilmId((IntegerHolder) null);
			assertNull(fo.getInteger(Film.FILM_ID));			
		}
	}
}
