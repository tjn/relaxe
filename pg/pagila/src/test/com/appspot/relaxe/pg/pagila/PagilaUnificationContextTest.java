/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.SimpleUnificationContext;
import com.appspot.relaxe.ent.im.EntityIdentityMap;
import com.appspot.relaxe.gen.pagila.ent.pub.Film;
import com.appspot.relaxe.gen.pagila.ent.pub.Film.Attribute;
import com.appspot.relaxe.gen.pagila.ent.pub.Film.Holder;
import com.appspot.relaxe.gen.pagila.ent.pub.Film.MetaData;
import com.appspot.relaxe.gen.pagila.ent.pub.Film.Reference;
import com.appspot.relaxe.gen.pagila.ent.pub.Film.Type;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;


public class PagilaUnificationContextTest extends AbstractPagilaTestCase {

	public void testUnify() {
//		if (System.currentTimeMillis() > 0)
//			throw new RuntimeException();
		
		
		SimpleUnificationContext c = new SimpleUnificationContext();
		Integer FID = Integer.valueOf(1);		
		
		Film.MetaData meta = Film.Type.TYPE.getMetaData();
		EntityIdentityMap<Attribute, Reference, Type, Film, Holder, MetaData> im = meta.getIdentityMap(c);
		EntityIdentityMap<Attribute, Reference, Type, Film, Holder, MetaData> im2 = meta.getIdentityMap(c);
		
		assertSame(im, im2);
		
		Film f1 = newEntity(Film.Type.TYPE);
		f1.getContent().setFilmId(FID);
		
		Film f2 = newEntity(Film.Type.TYPE);
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
