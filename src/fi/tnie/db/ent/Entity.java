/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.Map;

import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.IntervalHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DecimalKey;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntervalKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.TimeKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.meta.Column;
	
public interface Entity<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
> 
	extends
	Serializable
{	

	<	
		S extends Serializable,
		P extends PrimitiveType<P>,
		H extends PrimitiveHolder<S, P>,
		K extends PrimitiveKey<A, R, T, E, S, P, H, K>
	>	
	H get(K k);	


	<			
		P extends ReferenceType<P>,
		G extends Entity<?, ?, P, G>,
		H extends ReferenceHolder<?, ?, P, G>,
		K extends EntityKey<A, R, T, E, P, G, H, ? extends K>
	>	
	H getRef(K k);
	
	<
		S extends Serializable,
		P extends PrimitiveType<P>,
		H extends PrimitiveHolder<S, P>,
		K extends PrimitiveKey<A, R, T, E, S, P, H, K> 
	>	
	void set(K k, H newValue);
	

	
	public Entity<?, ?, ?, ?> getRef(R k);
	
	<			
		P extends ReferenceType<P>,
		G extends Entity<?, ?, P, G>,
		H extends ReferenceHolder<?, ?, P, G>,
		K extends EntityKey<A, R, T, E, P, G, H, ? extends K>
	>		
	void setRef(K k, H newValue);	
	
	PrimitiveHolder<?, ?> value(A attribute);
			
	
	/***
	 * Returns the value of the corresponding column.
	 * 
	 * If the <code>column</code> directly corresponds an attribute, the value of that attribute is returned.  
	 * Otherwise, the foreign keys the <code>column</code> is a part of are searched to find an entity reference.
	 * 
	 * If there is no referenced entity, <code>null</code> is returned.
	 * 
	 * If the entity reference <code>ref</code> (referenced by foreign key <code>F</code>) is found, 
	 * <code>column</code> is mapped to the corresponding column <code>fkcol</code> 
	 * in referenced table and result of the expression <code>ref.get(fkcol)</code> is returned.
	 *       
	 * @param column
	 * @return Scalar value or <code>null</code>, if the value is not set
	 * @throws NullPointerException If <code>c</code> is <code>null</code>.	 
	 */
	
	PrimitiveHolder<?, ?> get(Column c);	
	
	/***
	 * Returns the value of the corresponding column.
	 * 
	 * If the <code>column</code> directly corresponds an attribute, the value of that attribute is returned.  
	 * Otherwise, the foreign keys the <code>column</code> is a part of are searched to find an entity reference.
	 * 
	 * If there is no referenced entity, <code>null</code> is returned.
	 * 
	 * If the entity reference <code>ref</code> (referenced by foreign key <code>F</code>) is found, 
	 * <code>column</code> is mapped to the corresponding column <code>fkcol</code> 
	 * in referenced table and result of the expression <code>ref.get(fkcol)</code> is returned.
	 *       
	 * @param column
	 * @return Scalar value or <code>null</code>, if the value is not set
	 * @throws NullPointerException If <code>c</code> is <code>null</code>.	 
	 */
		
	ReferenceHolder<?, ?, ?, ?> ref(R ref);
	
	/**
	 * Set the value of the attribute <code>a</code>
	 * 
	 * @param a
	 * @param value
	 */
	void set(R r, ReferenceHolder<?, ?, ?, ?> value);
			
	EntityDiff<A, R, T, E> diff(E another);
		
	Map<Column, PrimitiveHolder<?, ?>> getPrimaryKey();
	
	/**
	 * Returns the meta-data object which describes the structure of this object.
	 * @return
	 */	
	EntityMetaData<A, R, T, E> getMetaData();
		
	T getType();
	
	ReferenceHolder<A, R, T, E> ref();
	
	IntegerHolder getInteger(IntegerKey<A, R, T, E> k);
	VarcharHolder getVarchar(VarcharKey<A, R, T, E> k);
	DateHolder getDate(DateKey<A, R, T, E> k);
	TimestampHolder getTimestamp(TimestampKey<A, R, T, E> k);
	
	TimeHolder getTime(TimeKey<A, R, T, E> k);
	CharHolder getChar(CharKey<A, R, T, E> k);
	DoubleHolder getDouble(DoubleKey<A, R, T, E> k);
	DecimalHolder getDecimal(DecimalKey<A, R, T, E> k);
	
	IntervalHolder.YearMonth getInterval(IntervalKey.YearMonth<A, R, T, E> k);
	IntervalHolder.DayTime getInterval(IntervalKey.DayTime<A, R, T, E> k);	
	
	void setInteger(IntegerKey<A, R, T, E> k, IntegerHolder newValue);
	void setVarchar(VarcharKey<A, R, T, E> k, VarcharHolder newValue);
	void setChar(CharKey<A, R, T, E> k, CharHolder newValue);
	void setDate(DateKey<A, R, T, E> k, DateHolder newValue);
	void setTimestamp(TimestampKey<A, R, T, E> k, TimestampHolder newValue);
	void setTime(TimeKey<A, R, T, E> k, TimeHolder newValue);
	void setDecimal(DecimalKey<A, R, T, E> k, DecimalHolder newValue);
	void setDouble(DoubleKey<A, R, T, E> k, DoubleHolder newValue);
	
	void setInterval(IntervalKey.YearMonth<A, R, T, E> k, IntervalHolder.YearMonth newValue);
	void setInterval(IntervalKey.DayTime<A, R, T, E> k, IntervalHolder.DayTime newValue);
	
			
	/**
	 * TODO: EntityQueryTask should be also rewritten to use DataObjectReader
	 */
	// DataObject getSource();
	
	public E self();
	
}
