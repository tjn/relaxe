/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;


import fi.tnie.db.query.QueryException;
import fi.tnie.db.test.MPAARating;
import fi.tnie.db.types.PrimitiveType;

public class TypeTest
	extends AbstractUnitTest
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
						
			dumpMetaData(st, "SELECT * FROM public.film");
			
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

	private void dumpMetaData(Statement st, String q) throws SQLException {
		ResultSet rs = st.executeQuery(q);
		ResultSetMetaData meta = rs.getMetaData();
		int cc = meta.getColumnCount();
		
		logger().debug(q);
		
		boolean content = rs.next();
		
		for (int i = 1; i <= cc; i++) {
			String label = meta.getColumnLabel(i);
			String name = meta.getColumnName(i);
			String className = meta.getColumnClassName(i);
			int type = meta.getColumnType(i);
			String typeName = meta.getColumnTypeName(i);
						
			Object o = content ? rs.getObject(i) : null;
			String s = content ? rs.getString(i) : null;
			
			logger().debug("ordinal   : " + i);
			logger().debug("label     : " + label);
			logger().debug("name      : " + name);
			logger().debug("type      : " + type + " (" + PrimitiveType.getSQLTypeName(type) + ")");
			logger().debug("typeName  : " + typeName);
			logger().debug("className : " + className);
			logger().debug("content   : " + content);
			logger().debug("object    : " + o);
			logger().debug("objType   : " + ((o == null) ? null : o.getClass().getName()));
			logger().debug("string    : " + (s));
			
			if (o != null && o instanceof java.sql.Array) {
				Array a = (Array) o;
				logger().debug("arrayBaseType   : " + a.getBaseType() + " (" + PrimitiveType.getSQLTypeName(a.getBaseType()) + ")");
				logger().debug("arrayBaseTypeName  : " + a.getBaseTypeName());
			}
			
						
			logger().debug("");
		}
		
		rs.close();
	}

}
