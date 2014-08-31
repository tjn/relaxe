package com.appspot.relaxe.pg.pagila;

import java.util.Collections;

import com.appspot.relaxe.gen.pg.pagila.ent.pub.Actor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.FilmActor;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Language;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.value.IntegerHolder;

public class AbstractPagilaEntityTestCase
	extends AbstractPagilaTestCase {

	protected Film.Mutable newFilm() {
		Film.Mutable fo = Film.Type.TYPE.getMetaData().getFactory().newEntity();
		return fo;
	}

	protected Actor.Mutable newActor() {
		return newEntity(Actor.Type.TYPE);
	}

	protected void testActorPrimaryKey(Actor pk) {
		testPrimaryKeyEntity(pk);		
		assertEquals(Collections.singleton(Actor.ACTOR_ID.name()), pk.attributes());		
	}

	protected void testFilmPrimaryKey(Film pk) {
		testPrimaryKeyEntity(pk);		
		assertEquals(Collections.singleton(Film.FILM_ID.name()), pk.attributes());		
	}

	protected void testFilmActorPrimaryKey(FilmActor pk) {
		testPrimaryKeyEntity(pk);
		assertTrue(pk.attributes().isEmpty());
				
		Film.Holder pkfilm = pk.getFilm(FilmActor.FILM);
		
		assertNotNull(pkfilm);
		testPrimaryKeyEntity(pkfilm.value());
						
		Actor a = pk.getActor().value();
		testActorPrimaryKey(a);
		
		Film f = pk.getFilm().value();
		testFilmPrimaryKey(f);
	}

	protected void testFilmActorPrimaryKey(FilmActor pk, int expectedFilmId,
			int expectedActorId) {
				testFilmActorPrimaryKey(pk);
				
				assertEquals(expectedFilmId, pk.getFilm(FilmActor.FILM).value().getFilmId().value().intValue());		
				assertEquals(expectedActorId, pk.getActor(FilmActor.ACTOR).value().getActorId().value().intValue());
				
			}

	protected void testFilmPrimaryKey(Film pk, int expectedId) {
		testPrimaryKeyEntity(pk);		
	
		assertEquals(1, pk.attributes().size());
		assertTrue(pk.attributes().contains(Film.Attribute.FILM_ID));
	
		IntegerHolder v = pk.getFilmId();
		assertNotNull(v);
		assertNotNull(v.value());		
		assertEquals(expectedId, v.value().intValue());
		
		assertNull(pk.getLanguage());
		assertNull(pk.getOriginalLanguage());
				
		try {
			pk.attributes().clear();
			fail("attributes cleared");
		}
		catch (UnsupportedOperationException e) {			
		}
		
		try {
			pk.attributes().add(Film.Attribute.DESCRIPTION);
			fail("attribute added");
		}
		catch (UnsupportedOperationException e) {			
		}
				
		try {
			pk.attributes().iterator().remove();
			fail("pk.attributes().iterator().remove()");
		}
		catch (UnsupportedOperationException e) {			
		}
		
		Film.Mutable fm = pk.toMutable();
		assertNotNull(fm);
		
		assertEquals(pk.attributes(), fm.attributes());
		assertTrue(fm == fm.asMutable());
		
	
		Language.Mutable lm = newEntity(Language.Type.TYPE);
		fm.setLanguage(lm);
		
		assertSame(lm, fm.getLanguage().value());
	
		
		
	}
	
	
}
