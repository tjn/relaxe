/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.types.IntervalType;
import fi.tnie.db.types.ReferenceType;

public class IntervalAccessor<
	A extends Attribute,	
	T extends ReferenceType<T, ?>,	
	E extends Entity<A, ?, T, E, ?, ?, ?>,
	V extends Interval<V>, 
	P extends IntervalType<P>, 
	H extends IntervalHolder<V, P>,
	K extends IntervalKey<A, T, E, V, P, H, K>
>
	extends AbstractPrimitiveAccessor<A, T, E, V, P, H, K> {

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
	
	public static class DayTime<
		A extends Attribute,
		T extends ReferenceType<T, ?>,
		E extends Entity<A, ?, T, E, ?, ?, ?>
	>
		extends IntervalAccessor<A, T, E, Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime, IntervalKey.DayTime<A, T, E>> {

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

		public DayTime(E target, IntervalKey.DayTime<A, T, E> k) {
			super(target, k);		
		}
	}
	

	public static class YearMonth<
		A extends Attribute,
		T extends ReferenceType<T, ?>,
		E extends Entity<A, ?, T, E, ?, ?, ?>
	>
		extends IntervalAccessor<A, T, E, Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth, IntervalKey.YearMonth<A, T, E>> {

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
		
		public YearMonth(E target, IntervalKey.YearMonth<A, T, E> k) {
			super(target, k);		
		}	
		
	}

}