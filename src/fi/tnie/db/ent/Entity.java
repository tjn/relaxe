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
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>	
> 
	extends
	Serializable
{	

	<	
		S extends Serializable,
		P extends PrimitiveType<P>,
		PH extends PrimitiveHolder<S, P>,
		K extends PrimitiveKey<A, T, E, S, P, PH, K>
	>	
	PH get(K k);	

	<			
		P extends ReferenceType<P, D>,
		G extends Entity<?, ?, P, G, RH, ?, D>,
		RH extends ReferenceHolder<?, ?, P, G, RH, D>,
		D extends EntityMetaData<?, ?, P, G, RH, ?, D>,
		K extends EntityKey<R, T, E, M, P, G, RH, D, K>
	>	
	RH getRef(K k);
	
	<
		S extends Serializable,
		P extends PrimitiveType<P>,
		RH extends PrimitiveHolder<S, P>,
		K extends PrimitiveKey<A, T, E, S, P, RH, K> 
	>	
	void set(K k, RH newValue);
	

	
	public Entity<?, ?, ?, ?, ?, ?, ?> getRef(R k);
	
	<			
		P extends ReferenceType<P, D>,
		G extends Entity<?, ?, P, G, RH, ?, D>,
		RH extends ReferenceHolder<?, ?, P, G, RH, D>,
		D extends EntityMetaData<?, ?, P, G, RH, ?, D>,
		K extends EntityKey<R, T, E, M, P, G, RH, D, K>
	>		
	void setRef(K k, RH newValue);	
	
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
	ReferenceHolder<?, ?, ?, ?, ?, ?> ref(R ref);
	
//	/**
//	 * Set the value of the reference <code>r</code>. TODO: this does not look safe.
//	 * 
//	 * @param a
//	 * @param value
//	 */
//	void set(R r, ReferenceHolder<?, ?, ?, ?, ?, ?> value);
			
	EntityDiff<A, R, T, E> diff(E another);
		
	Map<Column, PrimitiveHolder<?, ?>> getPrimaryKey();
	
	/**
	 * Returns the meta-data object which describes the structure of this object.
	 * @return
	 */
	
	M getMetaData();
		
	T getType();
	
	H ref();
	
	IntegerHolder getInteger(IntegerKey<A, T, E> k);
	VarcharHolder getVarchar(VarcharKey<A, T, E> k);
	DateHolder getDate(DateKey<A, T, E> k);
	TimestampHolder getTimestamp(TimestampKey<A, T, E> k);
	
	TimeHolder getTime(TimeKey<A, T, E> k);
	CharHolder getChar(CharKey<A, T, E> k);
	DoubleHolder getDouble(DoubleKey<A, T, E> k);
	DecimalHolder getDecimal(DecimalKey<A, T, E> k);
	
	IntervalHolder.YearMonth getInterval(IntervalKey.YearMonth<A, T, E> k);
	IntervalHolder.DayTime getInterval(IntervalKey.DayTime<A, T, E> k);	
	
	void setInteger(IntegerKey<A, T, E> k, IntegerHolder newValue);
	void setVarchar(VarcharKey<A, T, E> k, VarcharHolder newValue);
	void setChar(CharKey<A, T, E> k, CharHolder newValue);
	void setDate(DateKey<A, T, E> k, DateHolder newValue);
	void setTimestamp(TimestampKey<A, T, E> k, TimestampHolder newValue);
	void setTime(TimeKey<A, T, E> k, TimeHolder newValue);
	void setDecimal(DecimalKey<A, T, E> k, DecimalHolder newValue);
	void setDouble(DoubleKey<A, T, E> k, DoubleHolder newValue);
	
	void setInterval(IntervalKey.YearMonth<A, T, E> k, IntervalHolder.YearMonth newValue);
	void setInterval(IntervalKey.DayTime<A, T, E> k, IntervalHolder.DayTime newValue);
		
	public E self();

	E unify(IdentityContext ctx);
		
	boolean isIdentified();
		
}
