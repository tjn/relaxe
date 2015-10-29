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
package com.appspot.relaxe.pg.relaxetest;

import java.io.IOException;
import java.util.Comparator;

import com.appspot.relaxe.ent.EntityException;
import com.appspot.relaxe.env.pg.PGEnvironment;
import com.appspot.relaxe.env.pg.PGIdentifierRules;
import com.appspot.relaxe.expr.Identifier;
import com.appspot.relaxe.gen.pg.relaxetest.ent.pub.Obj;
import com.appspot.relaxe.gen.pg.relaxetest.ent.pub.Subobj;
import com.appspot.relaxe.gen.pg.relaxetest.ent.pub.Subobjref;
import com.appspot.relaxe.meta.BaseTable;
import com.appspot.relaxe.meta.Column;
import com.appspot.relaxe.meta.ColumnMap;
import com.appspot.relaxe.pg.relaxetest.test.AbstractRelaxetestTestCase;
import com.appspot.relaxe.service.ClosableDataAccessSession;
import com.appspot.relaxe.service.DataAccessException;
import com.appspot.relaxe.service.EntitySession;

public class NameTest
	extends AbstractRelaxetestTestCase {
	
	public void testComparator() throws Exception {
		
		PGEnvironment env = PGEnvironment.environment();
		
		PGIdentifierRules rules = env.getIdentifierRules();
		
		Identifier a = rules.toIdentifier("TYPE");
		Identifier b = rules.toIdentifier("type");
		
		Comparator<Identifier> cmp = rules.comparator();
		
		int result = cmp.compare(a, b);
		assertEquals(0, result);		
	}
	
	public void testTypeColumn() throws Exception {
		
		Obj.Type type = Obj.Type.TYPE;		
		Obj.MetaData md = type.getMetaData();
				
		BaseTable table = md.getBaseTable();		
		ColumnMap cm = table.getColumnMap();
		
		{
			Column col = cm.get(Obj.Attribute.ID.identifier());		
			assertNotNull(col);
		}
		
		{
			Column col = cm.get(Obj.Attribute.TYPE.name());		
			assertNotNull(col);
		}
		
		{
			Column col = cm.get(Obj.Attribute.TYPE.identifier());		
			assertNotNull(col);
		}		
	}
	
	
	public void testInsert() throws IOException, DataAccessException, EntityException {
		deleteAll(Subobjref.Type.TYPE.getMetaData().getBaseTable());
		deleteAll(Subobj.Type.TYPE.getMetaData().getBaseTable());
		deleteAll(Obj.Type.TYPE.getMetaData().getBaseTable());
		
		ClosableDataAccessSession das = newSession();		
		
		EntitySession es = das.asEntitySession();
				
		Obj.Mutable mo = es.newEntity(Obj.Type.TYPE);
		
		Integer oid = Integer.valueOf(1);		
		mo.setId(oid);
		mo.setName("name");
		mo.setType(Integer.valueOf(2));
		
		Obj obj = es.insert(mo.as());
		logger().info("obj: {}", obj);
		
		assertNotNull(obj);		
		assertTrue(obj.isIdentified());
				
		Subobj.Mutable so = es.newEntity(Subobj.Type.TYPE);
		so.setObj(obj);
		so.setIndex(oid);
		
		Subobj isub = es.insert(so.as());		
		logger().info("si: {}", isub);
		logger().info("si.obj: {}", isub.getObj().value());
		
		Subobjref.Mutable ref = es.newEntity(Subobjref.Type.TYPE);		
		ref.setObj(obj);
		ref.setSubobj(isub);
		ref.setRef(Integer.valueOf(1));
		
		es.update(ref.as());
						
		ref.setObj((Obj.Holder) null);
		ref.setSubobj(isub);
		ref.setRef(Integer.valueOf(1));

		
		Subobjref iref = es.insert(ref.as());
		
		logger().info("iref: {}", iref);
		
		das.commit();
		
	}
	

	
}
