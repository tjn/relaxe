/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent.value;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.rpc.Interval;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.types.IntervalType;

public class IntervalAccessor<
	A extends Attribute,
	E,
	V extends Interval<V>, 
	P extends IntervalType<P>, 
	H extends IntervalHolder<V, P, H>,
	K extends IntervalKey<A, E, V, P, H, K>
>
	extends AbstractPrimitiveAccessor<A, E, V, P, H, K> {

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
		E extends HasInterval.DayTime<A, E>
	>
		extends IntervalAccessor<A, E, Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime, IntervalKey.DayTime<A, E>> {

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
	

	public static class YearMonth<
		A extends Attribute,		
		E extends HasInterval.YearMonth<A, E>
	>
		extends IntervalAccessor<A, E, Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth, IntervalKey.YearMonth<A, E>> {

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