/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.mysql.samples;

import java.sql.Connection;

import com.appspot.relaxe.PersistenceManager;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.TriggerTest;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.TriggerTest.Attribute;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.TriggerTest.Content;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.TriggerTest.Factory;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.TriggerTest.Holder;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.TriggerTest.MetaData;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.TriggerTest.Reference;
import com.appspot.relaxe.gen.mysql.samples.ent.samples.TriggerTest.Type;
import com.appspot.relaxe.mysql.samples.MySQLSamplesTriggerTest;
import com.appspot.relaxe.rpc.IntegerHolder;



public class TriggerKeyTest 
	extends MySQLSamplesTriggerTest {
	
	public void testInsertTrigger() throws Exception {
		Connection c = newConnection();
		TriggerTest abc = TriggerTest.Type.TYPE.getMetaData().getFactory().newEntity();
		abc.getContent().setName("asdf");
		
		PersistenceManager<Attribute, Reference, Type, TriggerTest, Holder, Factory, MetaData, Content> pm = create(abc);
		pm.insert(c);
		
		IntegerHolder id = abc.getInteger(TriggerTest.ID);
		assertNotNull(id);
		assertNotNull(id.value());
		logger().debug("testInsertTrigger: id.value()=" + id.value());
		
		c.close();		
	}

}
