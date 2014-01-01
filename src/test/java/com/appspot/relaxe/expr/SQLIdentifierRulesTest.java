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
package com.appspot.relaxe.expr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appspot.relaxe.meta.SQLIdentifierRules;

import junit.framework.TestCase;

public class SQLIdentifierRulesTest extends TestCase {
	
	private static Logger logger = LoggerFactory.getLogger(SQLIdentifierRulesTest.class);
	
	private SQLIdentifierRules sir = new SQLIdentifierRules();

	public void testStartVisitContextIdentifier1() {
		StringBuilder buf = new StringBuilder();
		QueryGenerator g = new QueryGenerator(buf);
		Identifier ident = sir.toIdentifier("Lost+Found");
		assertFalse(ident.isOrdinary());
		ident.traverse(null, g);
		assertEquals("\"Lost+Found\"", buf.toString());
	}
	
	public void testStartVisitContextIdentifier2() {
		StringBuilder buf = new StringBuilder();
		QueryGenerator g = new QueryGenerator(buf);		
		Identifier ident = sir.toIdentifier("My_Table");
		assertTrue(ident.isOrdinary());
		ident.traverse(null, g);
		assertEquals("MY_TABLE", buf.toString());
	}

	
	
//	public void testStartVisitContextIdentifier4() {
//		StringBuilder buf = new StringBuilder();
//		QueryGenerator g = new QueryGenerator(buf, true);		
//		Identifier ident = AbstractIdentifier.create("My_Table", Folding.LOWERCASE);
//		assertTrue(ident.isOrdinary());
//		ident.traverse(null, g);
//		assertEquals("\"my_table\"", buf.toString());
//	}

	private static Logger logger() {
		return SQLIdentifierRulesTest.logger;
	}

}
