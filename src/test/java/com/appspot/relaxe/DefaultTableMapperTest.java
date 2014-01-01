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
package com.appspot.relaxe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.map.TableMapper;

import junit.framework.TestCase;

public class DefaultTableMapperTest extends TestCase {

	private static Logger logger = LoggerFactory.getLogger(DefaultTableMapperTest.class);
	
	public DefaultTableMapperTest() {		
	}
	
	public void testToJavaIdentifier() {
		TableMapper tm = newMapper();
				
		String[] eqi = { "Id", "ID", "id", "GC_ed" };
		
		for(String s : eqi)	{			
			testIdentityTransform(tm, s);
		}
	}
	
	public void testToJavaIdentifierEmpty() {
		TableMapper tm = newMapper();
		String jid = tm.toJavaIdentifier("");
		assertNotNull(jid);
		assertEquals("_", jid);		
	}
	

	public void testToJavaIdentifier2() {
		TableMapper tm = newMapper();
		String jid = tm.toJavaIdentifier("R&D  ");
		assertNotNull(jid);
		assertEquals("RD", jid);		
	}
	
	public void testToJavaIdentifier3() {		
		TableMapper tm = newMapper();
		String jid = tm.toJavaIdentifier("123");
		assertNotNull(jid);
		assertEquals("_123", jid);		
	}


	public void testToJavaIdentifier4() {
		TableMapper tm = newMapper();
		String jid = tm.toJavaIdentifier("a$e");
		assertNotNull(jid);
		assertEquals("a$e", jid);		
	}
	
	public void testToJavaIdentifier5() {
		TableMapper tm = newMapper();
		String jid = tm.toJavaIdentifier("צה");
		assertNotNull(jid);
		assertEquals("צה", jid);		
	}

	protected TableMapper newMapper() {
		TableMapper tm = new DefaultTableMapper("root");
		return tm;
	}

	protected void testIdentityTransform(TableMapper tm, String s) {		
		logger().debug("testIdentityTransform: input='" + s + "'");
		String jid = tm.toJavaIdentifier(s);
		assertNotNull(jid);
		assertEquals(s, jid);
	}

	
	private static Logger logger() {
		return DefaultTableMapperTest.logger;
	}
}
