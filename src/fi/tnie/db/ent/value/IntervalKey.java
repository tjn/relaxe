/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;


import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.types.IntervalType;
import fi.tnie.db.types.PrimitiveType;

public abstract class IntervalKey<
	V extends Interval<V>, 
	P extends PrimitiveType<P>, 
	H extends IntervalHolder<V, P>, 
	A extends Attribute, 
	E extends Entity<A, ?, ?, E>,
	K extends IntervalKey<V, P, H, A, E, K>
	>
	extends PrimitiveKey<A, V, P, H, E, K>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3226711133703096241L;

	/**
	 * No-argument constructor for GWT Serialization
	 */	
	protected IntervalKey() {
	}

	protected IntervalKey(EntityMetaData<A, ?, ?, E> meta, A name) {
		super(meta, name);
	}
	

	public static final class YearMonth<A extends Attribute, E extends Entity<A, ?, ?, E>>
		extends IntervalKey<Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth, A, E, 
			IntervalKey.YearMonth<A, E>> {
	
		/**
		 * 
		 */
		private static final long serialVersionUID = -8221115262736792946L;

		/**
		 * No-argument constructor for GWT Serialization
		 */	
		@SuppressWarnings("unused")
		private YearMonth() {
		}

		protected YearMonth(EntityMetaData<A, ?, ?, E> meta, A name) {
			super(meta, name);
			meta.addKey(this);
		}
		
		public static <
			X extends Attribute,
			T extends Entity<X, ?, ?, T>
		>
		YearMonth<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
			YearMonth<X, T> k = meta.getYearMonthIntervalKey(a);
		
			if (k == null) {
				PrimitiveType<?> t = a.type();
				
				if (t == IntervalType.YearMonth.TYPE) {
					k = new YearMonth<X, T>(meta, a);
				}
			}
					
			return k;
		}		

		@Override
		public fi.tnie.db.rpc.IntervalHolder.YearMonth get(E e) {	
			return e.getInterval(this);
		}

		@Override
		public fi.tnie.db.rpc.IntervalHolder.YearMonth newHolder(
				fi.tnie.db.rpc.Interval.YearMonth newValue) {
			return new IntervalHolder.YearMonth(newValue);
		}

		@Override
		public void set(E e, fi.tnie.db.rpc.IntervalHolder.YearMonth newValue) {
			e.setInterval(this, newValue);	
		}

		@Override
		public fi.tnie.db.types.IntervalType.YearMonth type() {
			return IntervalType.YearMonth.TYPE;
		}
		
	}
	
	public static final class DayTime<A extends Attribute, E extends Entity<A, ?, ?, E>>
		extends IntervalKey<Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime, A, E, 
			IntervalKey.DayTime<A, E>> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1349100151581333245L;
		
		public DayTime(EntityMetaData<A, ?, ?, E> meta, A a) {
			super(meta, a);
			meta.addKey(this);
		}

		public static <
			X extends Attribute,
			T extends Entity<X, ?, ?, T>
		>
		IntervalKey.DayTime<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {			
			IntervalKey.DayTime<X, T> k = meta.getDayTimeIntervalKey(a);
			
			if (k == null) {
				PrimitiveType<?> t = meta.getAttributeType(a);
				
				if (t == IntervalType.DayTime.TYPE) {
					k = new IntervalKey.DayTime<X, T>(meta, a);
				}			
			}
					
			return k;
		}

		@Override
		public fi.tnie.db.rpc.IntervalHolder.DayTime get(E e) {
			return e.getInterval(this);
		}

		@Override
		public fi.tnie.db.rpc.IntervalHolder.DayTime newHolder(fi.tnie.db.rpc.Interval.DayTime newValue) {
			return new IntervalHolder.DayTime(newValue);
		}

		@Override
		public void set(E e, fi.tnie.db.rpc.IntervalHolder.DayTime newValue) {
			e.setInterval(this, newValue);			
		}

		@Override
		public fi.tnie.db.types.IntervalType.DayTime type() {
			return IntervalType.DayTime.TYPE;
		}
	}

	
	
//	public static <
//		X extends Attribute,
//		T extends Entity<X, ?, ?, T>
//	>
//	IntervalKey<X, T> get(EntityMetaData<X, ?, ?, T> meta, X a) {
//		IntervalKey<X, T> k = meta.getTimestampKey(a);
//		
//		if (k == null) {
//			PrimitiveType<?> t = meta.getAttributeType(a);
//			
//			if (t != null && t.getSqlType() == PrimitiveType.TIMESTAMP) {
//				k = new IntervalKey<X, T>(meta, a);
//			}			
//		}
//				
//		return k;
//	}

//	@Override
//	public TimestampType type() {
//		return TimestampType.TYPE;
//	}
//
//	public void set(E e, TimestampHolder newValue) {
//		e.setTimestamp(this, newValue);
//	}
//	
//	public TimestampHolder get(E e) {
//		return e.getTimestamp(this);
//	}
//	
//	@Override
//	public TimestampHolder newHolder(Date newValue) {
//		return TimestampHolder.valueOf(newValue);
//	}
//	
//	@Override
//	public IntervalKey<A, E> normalize(EntityMetaData<A, ?, ?, E> meta) {
//		return meta.getTimestampKey(name());
//	}
	
}
