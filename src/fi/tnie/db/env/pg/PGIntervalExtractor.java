/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db.env.pg;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fi.tnie.db.IntervalExtractor;
import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.types.IntervalType;

public abstract class PGIntervalExtractor<V extends Interval<V>, T extends IntervalType<T>>	
{
	private static Pattern pattern = Pattern.compile(
			"(([+-]?[0-9]+) years? ?)?" +
			"(([+-]?[0-9]+) mons? ?)?" +
			"(([+-]?[0-9]+) days? ?)?" +
			" ?" +
			"(([+-]?)([0-9]{2}):([0-9]{2}):([0-9]{2}(.[0-9]+)?))?"
		);
	
	public static class YearMonth
		extends IntervalExtractor.YearMonth {

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
			
			Matcher pm = PGIntervalExtractor.pattern.matcher(s);
						
			if (!pm.matches()) {
				return null;
			}
			else {
				String yy = pm.group(2);
				String mm = pm.group(4);				
				
				int y = (yy == null) ? 0 : Integer.parseInt(yy);
				int m = (mm == null) ? 0 : Integer.parseInt(mm);
				
				ym = new IntervalHolder.YearMonth(new Interval.YearMonth(y, m));
			}
			
			return ym;			
		}
	}
	
	public static class DayTime
		extends IntervalExtractor.DayTime {

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
			
			Matcher pm = PGIntervalExtractor.pattern.matcher(str);
						
			if (!pm.matches()) {
				return null;
			}
			else {
				String ds = pm.group(6);				
				String sign = pm.group(8);
				sign = (sign == null) ? "" : sign;
				String hs = pm.group(9);
				String ms = pm.group(10);
				String ss = pm.group(11);
				
				
				int d = (ds == null) ? 0 : Integer.parseInt(ds); 
				int h = (hs == null) ? 0 : Integer.parseInt(sign + hs);
				int m = (ms == null) ? 0 : Integer.parseInt(sign + ms);
				double s = (ss == null) ? 0 : Double.parseDouble(sign + ss);
								
				ti = new IntervalHolder.DayTime(new Interval.DayTime(d, h, m, s));
			}
			
			return ti;			
		}
	}
}