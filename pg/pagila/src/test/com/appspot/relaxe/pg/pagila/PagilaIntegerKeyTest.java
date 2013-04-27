/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.value.HasInteger;
import com.appspot.relaxe.ent.value.HasIntegerKey;
import com.appspot.relaxe.ent.value.IntegerKey;
import com.appspot.relaxe.gen.pagila.ent.pub.Film;
import com.appspot.relaxe.types.IntegerType;

import junit.framework.TestCase;

public class PagilaIntegerKeyTest extends TestCase {

	public void testEquals() {		
//		LiteralCatalog litcat = LiteralCatalog.getInstance();		
		test(Film.Type.TYPE.getMetaData(), Film.Attribute.FILM_ID);
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
