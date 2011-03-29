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
	R,
	T extends ReferenceType<T>,	
	E extends Entity<A, R, T, E>,
	V extends Interval<V>, 
	P extends IntervalType<P>, 
	H extends IntervalHolder<V, P>,
	K extends IntervalKey<A, R, T, E, V, P, H, K>
>
	extends AbstractPrimitiveAccessor<A, R, T, E, V, P, H, K> {

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
		R,
		T extends ReferenceType<T>,
		E extends Entity<A, R, T, E>>
		extends IntervalAccessor<A, R, T, E, Interval.DayTime, IntervalType.DayTime, IntervalHolder.DayTime, IntervalKey.DayTime<A, R, T, E>> {

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

		public DayTime(E target, IntervalKey.DayTime<A, R, T, E> k) {
			super(target, k);		
		}
	}
	

	public static class YearMonth<
		A extends Attribute, 
		R,
		T extends ReferenceType<T>,
		E extends Entity<A, R, T, E>>
		extends IntervalAccessor<A, R, T, E, Interval.YearMonth, IntervalType.YearMonth, IntervalHolder.YearMonth, IntervalKey.YearMonth<A, R, T, E>> {

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
		
		public YearMonth(E target, IntervalKey.YearMonth<A, R, T, E> k) {
			super(target, k);		
		}	
		
	}

}