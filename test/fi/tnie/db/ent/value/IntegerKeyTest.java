/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.gen.ent.LiteralCatalog;
import fi.tnie.db.gen.ent.personal.Person;
import fi.tnie.db.gen.ent.personal.Person.Type;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.ReferenceType;
import junit.framework.TestCase;

public class IntegerKeyTest extends TestCase {

	public void testEquals() {
		
		LiteralCatalog litcat = LiteralCatalog.getInstance();
		EntityMetaData<fi.tnie.db.gen.ent.personal.Person.Attribute, fi.tnie.db.gen.ent.personal.Person.Reference, Type, Person> meta = litcat.newPersonalFactory().newPerson().getMetaData();		
		test(meta, Person.Attribute.ID);
	}
	
	private 
	<
		A extends Attribute,
		Y extends Reference, 
		Z extends ReferenceType<Z>,		
		E extends Entity<A, Y, Z, E>
	>
	void test(EntityMetaData<A, Y, Z, E> meta, A name) {
		IntegerKey<A, Y, Z, E> ik1 = IntegerKey.get(meta, name);
		assertNotNull(ik1);
		assertSame(IntegerType.TYPE, ik1.type());
		IntegerKey<A, Y, Z, E> ik2 = IntegerKey.get(meta, name);
		assertSame(ik1, ik2);				
	}	
}
