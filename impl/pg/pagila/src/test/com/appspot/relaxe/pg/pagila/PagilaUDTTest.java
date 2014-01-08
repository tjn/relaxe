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
package com.appspot.relaxe.pg.pagila;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import com.appspot.relaxe.TestContext;
import com.appspot.relaxe.meta.impl.pg.PGImplementation;
import com.appspot.relaxe.pg.pagila.test.AbstractPagilaTestCase;


public class PagilaUDTTest 
	extends AbstractPagilaTestCase {

	public void testLoad() throws Exception {		
		logger().debug("testLoad - enter");
		
		TestContext<PGImplementation> ts = getCurrent();
		Connection c = ts.newConnection();
		
		Statement st = c.createStatement();
		
		ResultSet rs = st.executeQuery("select a2_8 from public.default_test");
		
		if (rs.next()) {
			Array array = rs.getArray(1);
			
			logger().debug("baseType: " + array.getBaseType());
			String baseTypeName = array.getBaseTypeName();			
			logger().debug("baseTypeName: " + baseTypeName);			
		}
		
		logger().debug("testLoad - exit");
	}

}
