/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.rpc;

import java.io.Serializable;

/**
 * 
 * TODO: Should we have leading field precision property?
 * 
 * @author tnie
 *
 * @param <T>
 */
public abstract class Interval<T extends Interval<T>>
	implements Serializable, Comparable<T> {
		
	public Interval() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 9026687183505903401L;

	public static enum Class {
		YEAR_MONTH,
		DAY_TIME,
	}
	
	public abstract Interval.Class getIntervalClass();	
	public abstract int signum();
	
	private int abs(int v) {
		return (v < 0) ? -v : v;
	}	

	private int signum(int v) {
		return (v < 0) ? -1 : (v > 0) ? 1 : 0;
	}

	public static class YearMonth
		extends Interval<Interval.YearMonth> {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1778355091069490958L;
		private int year;
		private int month;
		
		public YearMonth() {
			super();
		}
		
		public YearMonth(int year, int month) {
			super();
									
			int ym = month / 12;
								
			year += ym;
			month -= (12 * ym);
			
			if (year > 0 && month < 0) {
				month += 12;
				year--;
			}
			
			if (year < 0 && month > 0) {
				year++;
				month -= 12;
			}
									
			this.year = year;
			this.month = month;
		}

		@Override
		public Class getIntervalClass() {		
			return Class.YEAR_MONTH;
		}

		public int getYear() {
			return year;
		}

		public int getMonth() {
			return month;
		}

		@Override
		public int signum() {
			return 
				year < 0 ? -1 :
				year > 0 ? +1 : 
				month < 0 ? -1 :
				month > 0 ? +1 :
				0
			;
		}
		
		@Override
		public int compareTo(YearMonth ym) {
			if (ym == null) {
				throw new NullPointerException("ym");
			}
			
			int yd = super.signum(this.year - ym.year);
			
			if (yd != 0) {
				return yd;
			}
			
			return super.signum(this.month - ym.month);
		}

		@Override
		public boolean equals(Object obj) {
			if (super.equals(obj)) {
				return true;
			}
			
			if (!getClass().equals(obj.getClass())) {
				return false;
			}			
			
			return compareTo((YearMonth) obj) == 0;			
		}		
		
		@Override
		public int hashCode() {		
			return (12 * this.year) + this.month;
		}
	}
		
	public static class DayTime
		extends Interval<Interval.DayTime>		
	{		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1725408023899795896L;
				
		private int day;
		private int hour;
		private int minute;
		private double second;
						
		public DayTime() {
			super();
		}

		public DayTime(int day, int hour) {
			this(day, hour, 0, 0);
		}
		
		public DayTime(int hour, int minute, double second) {
			this(0, hour, minute, second);
		}
		
		public DayTime(int day, int hour, int minute, double second) {
			super();
			
			int sm = ((int) second) / 60;
			minute += sm;
			second -= (60 * sm);
			
			if (minute > 0 && second < 0) {
				minute--;
				second += 60;
			}			
			else if (minute < 0 && second > 0) {
				minute++;
				second -= 60;				
			}
			
			int hm = minute / 60;
			hour += hm;
			minute -= (60 * hm);
			
			if (hour > 0 && minute < 0) {
				hour--;
				minute += 60;
			}	
			else if (hour < 0 && minute > 0) {
				hour++;
				minute -= 60;				
			}				
			
			int dh = hour / 24;
			day += dh;
			hour -= (24 * dh);
			
			if (day > 0 && hour < 0) {
				day--;
				hour += 24;
			} 
			else if (day < 0 && hour > 0) {
				day++;
				hour -= 24;
			}
			
			this.day = day;
			this.hour = hour;
			this.minute = minute;
			this.second = second;
		}

		@Override
		public Class getIntervalClass() {		
			return Class.DAY_TIME;
		}

		public int getHour() {
			return hour;
		}

		public int getMinute() {
			return minute;
		}

		public double getSecond() {
			return second;
		}

		public int getDay() {
			return day;
		}
		
		@Override
		public int signum() {
			return 
				day < 0 ? -1 :
				day > 0 ? +1 :
				hour < 0 ? -1 :
				hour > 0 ? +1 : 
				minute < 0 ? -1 :
				minute > 0 ? +1 : 
				second < 0 ? -1 :
				second > 0 ? +1 :
				0
			;
		}
		
		@Override
		public int compareTo(DayTime ti) {
			if (ti == null) {
				throw new NullPointerException("ti");
			}
			
			int dd = super.signum(this.day - ti.day);
			
			if (dd != 0) {
				return dd;
			}
			
			int hd = super.signum(this.hour - ti.hour);
			
			if (hd != 0) {
				return hd;
			}

			int md = super.signum(this.minute - ti.minute);
			
			if (md != 0) {
				return md;
			}
			
			return 
				this.second < ti.second ? -1 : 
				this.second > ti.second ? +1 :					
				0;			
		}		
		
		@Override
		public String toString() {
			StringBuffer buf = new StringBuffer();
						
			buf.append(signum() < 0 ? "-" : "+");
			buf.append(super.abs(day));
			buf.append(":");
			buf.append(super.abs(hour));
			buf.append(":");
			buf.append(super.abs(minute));
			buf.append(".");
			buf.append(abs(second));
			
			return buf.toString();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (super.equals(obj)) {
				return true;
			}
			
			if (!getClass().equals(obj.getClass())) {
				return false;
			}			
			
			return compareTo((DayTime) obj) == 0;			
		}
		
		@Override
		public int hashCode() {
			/**
			 * Double.doubleToLongBits(value) can not be use here (its not transliterable) 
			 */
			
			return day ^ hour ^ minute ^ Double.valueOf(second).hashCode();
		}		
	
		private double abs(double v) {
			return (v < 0) ? -v : v;
		}
	}

	

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			throw new NullPointerException("obj");
		}
		
		if (obj == this) {
			return true;
		}
		
		return false;		
	}	
}
