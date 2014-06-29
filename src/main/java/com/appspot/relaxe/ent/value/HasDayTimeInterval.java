package com.appspot.relaxe.ent.value;

import com.appspot.relaxe.ent.AttributeName;
import com.appspot.relaxe.value.IntervalHolder;

public interface HasDayTimeInterval<
	A extends AttributeName,
	R extends HasDayTimeInterval.Read<A, R, W>,
	W extends HasDayTimeInterval.Write<A, R, W>
>
{	
	R asRead();	
	W asWrite();	
	
	public interface Read<
		A extends AttributeName,
		R extends HasDayTimeInterval.Read<A, R, W>,
		W extends HasDayTimeInterval.Write<A, R, W>
	>
		extends HasDayTimeInterval<A, R, W> {
		/**
		* Returns the value by the key or <code>null</code> if the value is not currently present.
		* 
		* @param key
		* @return The value corresponding the key.
		* @throws NullPointerException If <code>key</code> is <code>null</code>.
		*/
		IntervalHolder.DayTime getInterval(IntervalAttribute.DayTime<A, R, W> key);		
	}
	
	public interface Write<
		A extends AttributeName,
		R extends HasDayTimeInterval.Read<A, R, W>,
		W extends HasDayTimeInterval.Write<A, R, W>
		> extends HasDayTimeInterval<A, R, W> {
		
		/**
		 * Sets the value by the key.
		 * 
		 * @param key
		 * @param newValue May be null.
		 * @throws NullPointerException If <code>key</code> is <code>null</code>.
		 */
		void setInterval(IntervalAttribute.DayTime<A, R, W> key, IntervalHolder.DayTime newValue);
	}
}
