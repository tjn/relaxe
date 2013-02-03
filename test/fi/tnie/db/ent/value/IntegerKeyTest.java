/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.gen.pg.ent.LiteralCatalog;
import fi.tnie.db.gen.pg.ent.pub.Film;
import fi.tnie.db.types.IntegerType;
import junit.framework.TestCase;

public class IntegerKeyTest extends TestCase {

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
