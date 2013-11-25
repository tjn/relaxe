/*
 * This file is part of Relaxe.
 * Copyright (c) 2013 Topi Nieminen
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
package com.appspot.relaxe.hsqldb;

import java.io.BufferedOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Types;
import com.appspot.relaxe.env.util.ResultSetWriter;


public class HSQLDBUDTTestCase
	extends AbstractHSQLDBTestCase {

	

	@Override
	public String getDatabase() {
		return "default";
	}


	public void testDump() throws Exception {
		Connection c = getCurrent().newConnection();
	
		DatabaseMetaData meta = c.getMetaData();
		
		logger().debug("testDump: meta.getURL()=" + meta.getURL());
		
		BufferedOutputStream out = new BufferedOutputStream(System.out, 64 * 1024);
		
		com.appspot.relaxe.env.util.ResultSetWriter rw = new ResultSetWriter(out, false);		
		
		rw.header("JavaType Info");
		rw.apply(meta.getTypeInfo());

		rw.header("UDTs");
		rw.apply(meta.getUDTs(null, "public", null, new int[] { Types.DISTINCT}));
		
	}
	
}
