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
package com.appspot.relaxe;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.appspot.relaxe.meta.impl.pg.PGTest;
import com.appspot.relaxe.query.QueryException;


public class PGBlobTest2
	extends PGTest
{
	
	public void testBlob() throws SQLException, QueryException, ClassNotFoundException {
		Connection c = getCurrent().newConnection();
		Statement st = null;
		
		try {			
			
			c.setAutoCommit(false);
			
			st = c.createStatement();
			
			ResultSet rs = st.executeQuery("SELECT picture FROM public.staff WHERE staff_id = 6");
			
			if (rs.next()) {
				
//				try {								
//					Blob blob = rs.getBlob(1);				
//					logger().debug("testBlob: blob.length()=" + blob.length());				
//					blob.free();
//				}
//				catch (Exception e) {
//					logger().error("BLOB-error: " + e.getMessage(), e);
//				}
				
				try {								
					long blobId = rs.getLong(1);				
					logger().debug("testBlob: blobId=" + blobId);
				}
				catch (Exception e) {
					logger().error("BLOB-error: " + e.getMessage(), e);
				}
				
				try {								
					InputStream in = rs.getBinaryStream(1);				
					logger().debug("testBlob: in.available()=" + in.available());
					
					byte[] dest = new byte[in.available()];
					int read = in.read(dest);
					logger().debug("testBlob: read=" + read);
					String s = new String(dest);
					logger().debug("testBlob: s=" + s);
					
					
					in.close();
				}
				catch (Exception e) {
					logger().error("BLOB-error: " + e.getMessage(), e);
				}
				
			}
									
			
		}
		finally {
			if (st != null) {
				st.close();
			}
						
			c.close();
		}
	}
}
