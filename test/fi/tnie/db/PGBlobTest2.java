/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import fi.tnie.db.meta.impl.pg.PGTest;
import fi.tnie.db.query.QueryException;

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
