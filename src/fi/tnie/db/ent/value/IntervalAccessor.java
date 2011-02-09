/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.types.IntervalType;

public class IntervalAccessor<
	V extends Interval<V>, 
	T extends IntervalType<T>, 
	H extends IntervalHolder<V, T>, 
	A extends Attribute, 
	E extends Entity<A, ?, ?, E>,
	K extends IntervalKey<V, T, H, A, E, K>
>
	extends PrimitiveAccessor<A, V, T, H, E, K> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6884358447490019294L;

	/**
	 * No-argument constructor for GWT Serialization
	 */
	protected IntervalAccessor() {
	}

	public IntervalAccessor(E target, K k) {
		super(target, k);
	}
	
	public static class DayTime<A extends Attribute, E extends Entity<A, ?, ?, E>>
		extends IntervalAccessor<Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime, A, E, IntervalKey.DayTime<A, E>> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6124569175841369365L;
				
		/**
			 * No-argument constructor for GWT Serialization
			 */
		@SuppressWarnings("unused")
		private DayTime() {	
		}

		public DayTime(E target, IntervalKey.DayTime<A, E> k) {
			super(target, k);		
		}
	}
	

	public static class YearMonth<A extends Attribute, E extends Entity<A, ?, ?, E>>
		extends IntervalAccessor<Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth, A, E, IntervalKey.YearMonth<A, E>> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7638187752897298152L;
		
		
		/**
		 * No-argument constructor for GWT Serialization
		 */
		@SuppressWarnings("unused")
		private YearMonth() {	
		}
		
		public YearMonth(E target, IntervalKey.YearMonth<A, E> k) {
			super(target, k);		
		}	
		
	}

}