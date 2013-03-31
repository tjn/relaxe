/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg.pagila;

import fi.tnie.db.SimpleUnificationContext;
import fi.tnie.db.ent.im.EntityIdentityMap;
import fi.tnie.db.gen.pagila.ent.pub.Film;
import fi.tnie.db.gen.pagila.ent.pub.PublicFactory;
import fi.tnie.db.gen.pagila.ent.pub.Film.Attribute;
import fi.tnie.db.gen.pagila.ent.pub.Film.Holder;
import fi.tnie.db.gen.pagila.ent.pub.Film.MetaData;
import fi.tnie.db.gen.pagila.ent.pub.Film.Reference;
import fi.tnie.db.gen.pagila.ent.pub.Film.Type;
import fi.tnie.db.gen.pagila.ent.pub.impl.PublicFactoryImpl;
import junit.framework.TestCase;

public class PagilaUnificationContextTest extends TestCase {

	public void testUnify() {
//		if (System.currentTimeMillis() > 0)
//			throw new RuntimeException();
		
		
		SimpleUnificationContext c = new SimpleUnificationContext();
		Integer FID = Integer.valueOf(1);		
		
		PublicFactory pf = new PublicFactoryImpl();
		
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		EntityIdentityMap<Attribute, Reference, Type, Film, Holder, MetaData> im = meta.getIdentityMap(c);
		EntityIdentityMap<Attribute, Reference, Type, Film, Holder, MetaData> im2 = meta.getIdentityMap(c);
		
		assertSame(im, im2);
		
		Film f1 = pf.newFilm();
		f1.getContent().setFilmId(FID);
		
		Film f2 = pf.newFilm();
		f2.getContent().setFilmId(FID);
		
		Film.Holder h1 = im.get(f1);
		assertNotNull(h1);
		assertNotNull(h1.value());
		assertSame(f1, h1.value());
		
		Film.Holder h2 = im.get(f2);
		assertNotNull(h1);
		assertNotNull(h1.value());
				
		assertSame(f1, h2.value());
				
		c.close();
		
		EntityIdentityMap<Attribute, Reference, Type, Film, Holder, MetaData> im3 = meta.getIdentityMap(c);
		assertNotSame(im2, im3);
		c.close();
	}

}
