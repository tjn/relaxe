/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.types.IntervalType;

public abstract class SQLIntervalExtractor<V extends Interval<V>, T extends IntervalType<T>>	
{
//	private static Logger logger = Logger.getLogger(SQLIntervalExtractor.class);
	
	public static class YearMonth
		extends IntervalExtractor.YearMonth {
		
		private static Pattern pattern = Pattern.compile(
				"^([+-]?)([0-9]+)-([0-9]+)$"
			);		

		public YearMonth(int column) {
			super(column);
		}
		 
		@Override
		public IntervalHolder.YearMonth extractValue(
				ResultSet rs) throws SQLException {
			String s = rs.getString(getColumn());
			return create(s);
		}		
		
		public IntervalHolder.YearMonth create(String s) {
			if (s == null) {
				return IntervalHolder.YearMonth.NULL;
			}
			
			IntervalHolder.YearMonth ym = null;			
			
			Matcher pm = SQLIntervalExtractor.YearMonth.pattern.matcher(s);
						
			if (!pm.matches()) {
				return null;
			}
			else {
				String sign = pm.group(1);
				sign = (sign == null) ? "" : sign;
				
				String yy = pm.group(2);
				String mm = pm.group(3);				
				
				int y = (yy == null) ? 0 : Integer.parseInt(sign + yy);
				int m = (mm == null) ? 0 : Integer.parseInt(sign + mm);
				
				ym = new IntervalHolder.YearMonth(new Interval.YearMonth(y, m));
			}
			
			return ym;			
		}
	}
	
	public static class DayTime
		extends IntervalExtractor.DayTime {
			
		private static Pattern pattern = Pattern.compile(
				"^(([+-]?[0-9]+) )?([+-]?)([0-9]+):([0-9]+)?(:([0-9]{2}(.[0-9]+)?))?$"
			);		

		public DayTime(int column) {
			super(column);
		}
	 
		@Override
		public IntervalHolder.DayTime extractValue(
				ResultSet rs) throws SQLException {
			String s = rs.getString(getColumn());
			return create(s);
		}
	
	
		public IntervalHolder.DayTime create(String str) {
			if (str == null) {
				return IntervalHolder.DayTime.NULL;
			}
			
			IntervalHolder.DayTime ti = null;			
			
			Matcher pm = SQLIntervalExtractor.DayTime.pattern.matcher(str);
									
			if (!pm.matches()) {
				return null;
			}
			else {				
//				for (int i = 0; i < pm.groupCount(); i++) {
//					logger().debug("create: g(" + i + ")=" + pm.group(i));
//				}				
				
				String ds = pm.group(2);
				
				String tsg = pm.group(3);
				tsg = (tsg == null) ? "" : tsg;
				
//				logger().debug("create: tsg=" + tsg);
				
				String hs = pm.group(4);
				
//				logger().debug("create: hs=" + hs);
				
				String ms = pm.group(5);
				String ss = pm.group(7);
								
				int d = (ds == null) ? 0 : Integer.parseInt(ds); 
				int h = (hs == null) ? 0 : Integer.parseInt(tsg + hs);
				int m = (ms == null) ? 0 : Integer.parseInt(tsg + ms);
				double s = (ss == null) ? 0 : Double.parseDouble(tsg + ss);
								
				ti = new IntervalHolder.DayTime(new Interval.DayTime(d, h, m, s));
			}
			
			return ti;			
		}
	}
	
	
//	private static Logger logger() {
//		return SQLIntervalExtractor.logger;
//	}
}