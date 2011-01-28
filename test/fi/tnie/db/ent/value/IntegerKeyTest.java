/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.gen.ent.LiteralCatalog;
import fi.tnie.db.gen.ent.personal.LiteralPerson;
import fi.tnie.db.gen.ent.personal.Person;
import fi.tnie.db.gen.ent.personal.Person.Reference;
import fi.tnie.db.gen.ent.personal.Person.Type;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.PrimitiveType;
import junit.framework.TestCase;

public class IntegerKeyTest extends TestCase {

	public void testEquals() {
		
		LiteralCatalog litcat = LiteralCatalog.getInstance();
		EntityMetaData<fi.tnie.db.gen.ent.personal.Person.Attribute, Reference, Type, Person> meta = litcat.newPersonalFactory().newPerson().getMetaData();
		
		test(meta, Person.Attribute.ID);
	}
	
	private 
	<
		A extends Attribute,
		E extends Entity<A, ?, ?, E>
	>
	void test(EntityMetaData<A, ?, ?, E> meta, A name) {
		IntegerKey<A, E> ik1 = IntegerKey.get(meta, name);
		assertNotNull(ik1);
		assertSame(IntegerType.TYPE, ik1.type());
		IntegerKey<A, E> ik2 = IntegerKey.get(meta, name);
		assertSame(ik1, ik2);
				
	}
	
}
