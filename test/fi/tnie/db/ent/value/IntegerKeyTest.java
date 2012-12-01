/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.gen.pg.ent.LiteralCatalog;
import fi.tnie.db.gen.pg.ent.personal.Person;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.ReferenceType;
import junit.framework.TestCase;

public class IntegerKeyTest extends TestCase {

	public void testEquals() {
		
		LiteralCatalog litcat = LiteralCatalog.getInstance();
		Person.MetaData meta = litcat.newPersonalFactory().newPerson().getMetaData();		
		test(meta, Person.Attribute.ID);
	}
	
	private 
	<
		A extends Attribute,
		Y extends Reference, 
		Z extends ReferenceType<A, Y, Z, E, ?, ?, M, C>,		
		E extends Entity<A, Y, Z, E, ?, ?, M, C>,
		M extends EntityMetaData<A, Y, Z, E, ?, ?, M, C>,
		C extends Content
	>
	void test(M meta, A name) {
		IntegerKey<A, Z, E> ik1 = IntegerKey.get(meta, name);
		assertNotNull(ik1);
		assertSame(IntegerType.TYPE, ik1.type());
		IntegerKey<A, Z, E> ik2 = IntegerKey.get(meta, name);
		assertSame(ik1, ik2);				
	}	
}
