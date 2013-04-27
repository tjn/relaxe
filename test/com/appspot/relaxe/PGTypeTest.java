/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.postgresql.util.PGobject;

import com.appspot.relaxe.QueryHelper;
import com.appspot.relaxe.meta.impl.pg.PGTest;
import com.appspot.relaxe.pg.pagila.MPAARating;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.types.PrimitiveType;



public class PGTypeTest
	extends PGTest
{
	
	public void testColumnTypes() throws SQLException, QueryException, ClassNotFoundException {
		Connection c = getCurrent().newConnection();
		Statement st = null;
		
		try {			
			// logger().debug("testColumnTypes: cat.getName()=" + cat.getName());
			
//			getImplementation().
			
			c.setAutoCommit(false);
			
			{
				Object[] ia = { 1, 2, 3 };											
				Array arr = c.createArrayOf("int", ia);
				logger().debug("testColumnTypes: int-array=" + arr);
				logger().debug("testColumnTypes: int-array, base-type=" + arr.getBaseType());
				logger().debug("testColumnTypes: int-array, base-type-name=" + arr.getBaseTypeName());				
			}
			
					
			st = c.createStatement();
									
//			logger().debug("client-info:" + c.getClientInfo());
						
			dumpMetaDataWithSamples(st, "SELECT * FROM public.film");
			
			DatabaseMetaData meta = c.getMetaData();
			
			ResultSet cols = meta.getColumns(null, "public", "film", null);
			
			while (cols.next()) {				
				
				logger().debug("name: " + cols.getString(4));				
				int type = cols.getInt(5);				
				logger().debug("type: " + type + " (" + PrimitiveType.getSQLTypeName(type) + ")");
			}
			
			

			{
				PreparedStatement ps = c.prepareStatement("UPDATE public.film SET special_features = ? WHERE film_id = -1");
	
				{
					String[] data = { "test_features" };											
					Array array = c.createArrayOf("text", data);
					
					
					Object ao = array.getArray();
					
					logger().debug("testColumnTypes: ao.getClass()=" + ao.getClass());
					logger().debug("testColumnTypes: ao instanceof Object[]=" + (ao instanceof Object[]));
					logger().debug("testColumnTypes: ao instanceof String[]=" + (ao instanceof String[]));
					
									
					
					ps.setArray(1, array);
					ps.executeUpdate();
				}
	
				{
					String[] vd = { "test_features" };											
					Array va = c.createArrayOf("varchar", vd);		
					ps.setArray(1, va);				
					ps.executeUpdate();
				}
				
				c.commit();
				ps.close();
			}
			
			{
				PreparedStatement ps = c.prepareStatement("UPDATE public.film SET rating = ? WHERE film_id = -1");
	
				{			
					ps.setObject(1, MPAARating.G.value(), Types.OTHER);
					ps.executeUpdate();
					c.commit();
				}
	
//				{
//					ps.setString(1, MPAARating.G.value());
//					ps.executeUpdate();
//					c.commit();
//				}
				
				
				ps.close();
			}			
//			dumpMetaData(st, "SELECT cast(release_year as year) FROM public.film");
			
			
//			st.
			
			
			
		}
		finally {
			if (st != null) {
				st.close();
			}
						
			c.close();
		}
	}
	
	public void testFullText() throws Exception {
		Connection c = null;
		Statement st = null;
		
		try {
			c = getCurrent().newConnection();
			st = c.createStatement();
		
			ResultSet rs = st.executeQuery("SELECT film_id, fulltext FROM public.film WHERE film_id = 1");
			
			if (rs.next()) {
				Object fulltext = rs.getObject(2);				
				logger().debug("testFullText: fulltext=" + fulltext);
				logger().debug("testFullText: fulltext-type=" + ((fulltext == null) ? null : fulltext.getClass()));
				
				PGobject pgo = (PGobject) fulltext;
				logger().debug("testFullText: pgo.getType()=" + pgo.getType());
				logger().debug("testFullText: pgo.getValue()=" + pgo.getValue());
				
				
				String fts = rs.getString(2);
				logger().debug("testFullText: fts=" + fts);
				
				
				
			}
			
			rs = QueryHelper.doClose(rs);
			
			
		}
		finally {
			close(st);
			close(c);
		}
	}

}
