package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.value.IntervalHolder;

public interface HasYearMonthInterval<
	A extends AttributeName,
	R extends HasYearMonthInterval.Read<A, R, W>,
	W extends HasYearMonthInterval.Write<A, R, W>
>
{	
	R asRead();	
	W asWrite();	
	
	public interface Read<
		A extends AttributeName,
		R extends HasYearMonthInterval.Read<A, R, W>,
		W extends HasYearMonthInterval.Write<A, R, W>
	>
		extends HasYearMonthInterval<A, R, W> {
		/**
		* Returns the value by the key or <code>null</code> if the value is not currently present.
		* 
		* @param key
		* @return The value corresponding the key.
		* @throws NullPointerException If <code>key</code> is <code>null</code>.
		*/
		IntervalHolder.YearMonth getInterval(IntervalAttribute.YearMonth<A, R, W> key);		
	}
	
	public interface Write<
		A extends AttributeName,
		R extends HasYearMonthInterval.Read<A, R, W>,
		W extends HasYearMonthInterval.Write<A, R, W>
		> extends HasYearMonthInterval<A, R, W> {
		
		/**
		 * Sets the value by the key.
		 * 
		 * @param key
		 * @param newValue May be null.
		 * @throws NullPointerException If <code>key</code> is <code>null</code>.
		 */
		void setInterval(IntervalAttribute.YearMonth<A, R, W> key, IntervalHolder.YearMonth newValue);
	}
}
