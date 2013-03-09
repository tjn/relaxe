/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.meta.impl.pg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;

public class PGIntervalTest
	extends PGJDBCTestCase {
		
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testIntervalType() throws Exception {
		Connection c = getConnection();
		c.setAutoCommit(false);
		assertNotNull(c);
		int u = 0;
		
		Statement st = c.createStatement();
		String t = "interval_test";
		
		String s = "DROP TABLE IF EXISTS " + t;
		u = st.executeUpdate(s);
		logger().debug("testIntervalType: u=" + u);
		
		s = "CREATE TABLE " + t + "(" +
				"ID serial not null," +
				"yy interval year," +
				"mm interval month," +
				"dd interval day," +
				"hh interval hour," +
				"mi interval hour," +
				"se interval hour," +
				"ym interval year to month," +				
				"dh interval day to hour," +
				"dm interval day to minute," +
				"ds interval day to second," +
				"hm interval hour to minute," +
				"hs interval hour to second," +
				"ms interval minute to second," +
				
				"primary key (id)" +
				")";
		
		ls(s);
		u = st.executeUpdate(s);
		

//		s = "set interval_style=sql_standard";
//		ls(s);
//		u = st.executeUpdate(s);

		
		
		s = "insert into " + t + "(" +
				"yy, mm, dd, hh, mi, se, ym, dh, dm, ds, hm, hs, ms" +
				") values (" +
				" ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?,  ?" +
				")";
		
		ls(s);
		PreparedStatement ps = c.prepareStatement(s);
		
		int col = 1;
		
		ps.setObject(col++, "10", Types.OTHER); // yy
		ps.setObject(col++, "11", Types.OTHER);
		ps.setObject(col++, "12", Types.OTHER);
		ps.setObject(col++, "13", Types.OTHER);
		ps.setObject(col++, "14", Types.OTHER);  
		ps.setObject(col++, "15", Types.OTHER);  // se
		ps.setObject(col++, "1-2", Types.OTHER); // ym
//		ps.setObject(col++, "15 02:01", Types.OTHER);
		ps.setObject(col++, "15 2:0", Types.OTHER);
		ps.setObject(col++, "16 03:04", Types.OTHER);
		ps.setObject(col++, "17 05:06:07", Types.OTHER);
		ps.setObject(col++, "08:09", Types.OTHER);
		ps.setObject(col++, "10:11:12.00", Types.OTHER);
		ps.setObject(col++, "13:14.567", Types.OTHER);
		
		u = ps.executeUpdate();
		logger().debug("testIntervalType: u=" + u);
		
		c.commit();
		ps.close();
		
		s = "select * from " + t;
		ls(s);
		
		ResultSet rs = st.executeQuery(s);
		ResultSetMetaData meta = rs.getMetaData();
		
		int count = meta.getColumnCount();
		
		for (int i = 1; i <= count; i++) {
			String cl = meta.getColumnLabel(i);
			String cn = meta.getColumnName(i);
			String tn = meta.getColumnTypeName(i);
			int ct = meta.getColumnType(i);
			String cc = meta.getColumnClassName(i);
			int cp = meta.getPrecision(i);
			int cz = meta.getColumnDisplaySize(i);
			int cs = meta.getScale(i);
			boolean ca = meta.isAutoIncrement(i);
			boolean ch = meta.isSearchable(i);
					
			logger().debug("i=" + i);		
			
			logger().debug("cl=" + cl);
			logger().debug("cn=" + cn);
			logger().debug("tn=" + tn);
			logger().debug("ct=" + ct);
			logger().debug("cc=" + cc);
			logger().debug("cp=" + cp);
			logger().debug("cs=" + cs);
			logger().debug("ca=" + ca);
			logger().debug("cz=" + cz);
			logger().debug("ch=" + ch + "\n");						
		}
		
		
		int row = 0;
 		
		while(rs.next()) {
			logger().debug("row=" + (++row));
			
			for (int i = 1; i <= count; i++) {
				Object o = rs.getObject(i);
				logger().debug("o=" + o);
				
				String v = rs.getString(i);
				logger().debug("v=" + v);
			}
			
			logger().debug("");
		}
		
		rs.close();
		
		ps.close();
		
	}

	private void ls(String s) {
		logger().debug("executing (\n" + s + ")\n");
	}

}
