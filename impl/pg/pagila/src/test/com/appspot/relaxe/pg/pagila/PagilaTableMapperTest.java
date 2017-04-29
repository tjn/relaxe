package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.MutableEntity;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Customer;
import com.appspot.relaxe.gen.pg.pagila.ent.pub.Customer.Type;
import com.appspot.relaxe.map.JavaType;
import com.appspot.relaxe.map.TableMapper;
import com.appspot.relaxe.map.TableMapper.Part;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;
import com.appspot.relaxe.types.ReferenceType;
import com.appspot.relaxe.value.ReferenceHolder;


public class PagilaTableMapperTest
	extends AbstractPagilaTestCase {
	
	public void testEntityType() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		TableMapper tm = getDefaultTableMapper();
		
		Customer.Type eto = Customer.Type.TYPE;
		Class<? extends Type> etc = eto.getClass();
		
		BaseTable bt = Customer.Type.TYPE.getMetaData().getBaseTable();
		
		JavaType tt = tm.entityType(bt, Part.TYPE);
		assertNotNull(tt);
		
		String qn = tt.getQualifiedName();
		
		Class<?> ttc = Class.forName(qn);		
		Object tto = ttc.newInstance();
		
		assertTrue(etc.isInstance(tto));
		
		assertEquals(tto, eto);
		assertEquals(eto, tto);
		assertEquals(eto, tto);
		
		assertEquals(eto.hashCode(), tto.hashCode());
		
		ReferenceType<?, ?, ?, ?, ?, ?, ?, ?> rt = (ReferenceType<?, ?, ?, ?, ?, ?, ?, ?>) tto;
		
		test(rt);
	}

	private <
		A extends AttributeName,
		R extends Reference,
		T extends ReferenceType<A, R, T, E, B, H, F, M>,
		E extends Entity<A, R, T, E, B, H, F, M>,
		B extends MutableEntity<A, R, T, E, B, H, F, M>,
		H extends ReferenceHolder<A, R, T, E, H, M>,
		F extends EntityFactory<E, B, H, M, F>,
		M extends EntityMetaData<A, R, T, E, B, H, F, M>
	>
	void test(ReferenceType<A, R, T, E, B, H, F, M> rt) {
		M meta = rt.getMetaData();
		assertNotNull(meta);
	}
	
}
