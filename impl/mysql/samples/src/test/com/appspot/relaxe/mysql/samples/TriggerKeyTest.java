/*
 * This file is part of Relaxe.
 * Copyright (c) 2014 Topi Nieminen
 * Author: Topi Nieminen <topi.nieminen@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA.
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
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
