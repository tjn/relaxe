/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.mysql.samples;

import java.sql.Connection;

import fi.tnie.db.PersistenceManager;
import fi.tnie.db.gen.samples.ent.samples.TriggerTest;
import fi.tnie.db.gen.samples.ent.samples.TriggerTest.Attribute;
import fi.tnie.db.gen.samples.ent.samples.TriggerTest.Content;
import fi.tnie.db.gen.samples.ent.samples.TriggerTest.Factory;
import fi.tnie.db.gen.samples.ent.samples.TriggerTest.Holder;
import fi.tnie.db.gen.samples.ent.samples.TriggerTest.MetaData;
import fi.tnie.db.gen.samples.ent.samples.TriggerTest.Reference;
import fi.tnie.db.gen.samples.ent.samples.TriggerTest.Type;
import fi.tnie.db.mysql.samples.MySQLSamplesTriggerTest;
import fi.tnie.db.rpc.IntegerHolder;


public class TriggerKeyTest 
	extends MySQLSamplesTriggerTest {
	
	public void testInsertTrigger() throws Exception {
		Connection c = newConnection();
		TriggerTest abc = TriggerTest.Type.TYPE.getMetaData().getFactory().newInstance();
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
