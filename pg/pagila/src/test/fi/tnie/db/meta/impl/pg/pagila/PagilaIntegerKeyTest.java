/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg.pagila;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.value.HasInteger;
import fi.tnie.db.ent.value.HasIntegerKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.gen.pagila.ent.LiteralCatalog;
import fi.tnie.db.gen.pagila.ent.pub.Film;
import fi.tnie.db.types.IntegerType;
import junit.framework.TestCase;

public class PagilaIntegerKeyTest extends TestCase {

	public void testEquals() {		
		LiteralCatalog litcat = LiteralCatalog.getInstance();
		Film.MetaData meta = litcat.newPublicFactory().newFilm().getMetaData();
		test(meta, Film.Attribute.FILM_ID);
	}
	
	private 
	<
		A extends Attribute,
		E extends HasInteger<A, E>,
		M extends HasIntegerKey<A, E>
	>
	void test(M meta, A name) {
		IntegerKey<A, E> ik1 = IntegerKey.get(meta, name);
		assertNotNull(ik1);
		assertSame(IntegerType.TYPE, ik1.type());
		IntegerKey<A, E> ik2 = IntegerKey.get(meta, name);
		assertSame(ik1, ik2);				
	}	
}
