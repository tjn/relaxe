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
	T extends ReferenceType<A, R, T, E, H, F, M>,
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
	PH get(K k)
		throws EntityRuntimeException;	

	<	
		VT extends ReferenceType<VA, VR, VT, V, RH, VF, D>,
		VA extends Attribute,
		VR extends Reference,		
		V extends Entity<VA, VR, VT, V, RH, VF, D>,
		RH extends ReferenceHolder<VA, VR, VT, V, RH, D>,
		VF extends EntityFactory<V, RH, D, VF>,
		D extends EntityMetaData<VA, VR, VT, V, RH, VF, D>,
		K extends EntityKey<R, T, E, M, VT, VA, VR, V, RH, VF, D, K>
	>	
	RH getRef(K k);
	
	<
		S extends Serializable,
		P extends PrimitiveType<P>,
		RH extends PrimitiveHolder<S, P>,
		K extends PrimitiveKey<A, T, E, S, P, RH, K> 
	>	
	void set(K k, RH newValue)
		throws EntityRuntimeException;
	

	
	public Entity<?, ?, ?, ?, ?, ?, ?> getRef(R k);
	
	<			
		VT extends ReferenceType<VA, VR, VT, G, RH, VF, D>,
		VA extends Attribute,
		VR extends Reference,
		G extends Entity<VA, VR, VT, G, RH, VF, D>,
		RH extends ReferenceHolder<VA, VR, VT, G, RH, D>,
		VF extends EntityFactory<G, RH, D, VF>,
		D extends EntityMetaData<VA, VR, VT, G, RH, VF, D>,		
		K extends EntityKey<R, T, E, M, VT, VA, VR, G, RH, VF, D, K>
	>		
	void setRef(K k, RH newValue);	
	
	PrimitiveHolder<?, ?> value(A attribute) throws EntityRuntimeException;
	
				
	
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
	 * @throws EntityRuntimeException
	 * @throws NullPointerException If <code>c</code> is <code>null</code>.	 
	 */
	
	PrimitiveHolder<?, ?> get(Column c) throws NullPointerException, EntityRuntimeException;	
	
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
	
			
	EntityDiff<A, R, T, E> diff(E another) throws EntityRuntimeException;
		
	Map<Column, PrimitiveHolder<?, ?>> getPrimaryKey() throws EntityRuntimeException;
	
	/**
	 * Returns the meta-data object which describes the structure of this object.
	 * @return
	 */
	
	M getMetaData();
		
	T getType();
	
	H ref();
	
	IntegerHolder getInteger(IntegerKey<A, T, E> k)
		throws EntityRuntimeException;
	VarcharHolder getVarchar(VarcharKey<A, T, E> k)
		throws EntityRuntimeException;
	DateHolder getDate(DateKey<A, T, E> k)
		throws EntityRuntimeException;
	TimestampHolder getTimestamp(TimestampKey<A, T, E> k)
		throws EntityRuntimeException;	
	TimeHolder getTime(TimeKey<A, T, E> k)
		throws EntityRuntimeException;
	CharHolder getChar(CharKey<A, T, E> k)
		throws EntityRuntimeException;
	DoubleHolder getDouble(DoubleKey<A, T, E> k)
		throws EntityRuntimeException;
	DecimalHolder getDecimal(DecimalKey<A, T, E> k)
		throws EntityRuntimeException;
	
	IntervalHolder.YearMonth getInterval(IntervalKey.YearMonth<A, T, E> k);
	IntervalHolder.DayTime getInterval(IntervalKey.DayTime<A, T, E> k);	
	
	void setInteger(IntegerKey<A, T, E> k, IntegerHolder newValue)
		throws EntityRuntimeException;
	void setVarchar(VarcharKey<A, T, E> k, VarcharHolder newValue)
		throws EntityRuntimeException;
	void setChar(CharKey<A, T, E> k, CharHolder newValue)
		throws EntityRuntimeException;
	void setDate(DateKey<A, T, E> k, DateHolder newValue)	
		throws EntityRuntimeException;
	void setTimestamp(TimestampKey<A, T, E> k, TimestampHolder newValue)
		throws EntityRuntimeException;	
	void setTime(TimeKey<A, T, E> k, TimeHolder newValue)
		throws EntityRuntimeException;
	void setDecimal(DecimalKey<A, T, E> k, DecimalHolder newValue)
		throws EntityRuntimeException;
	void setDouble(DoubleKey<A, T, E> k, DoubleHolder newValue)
		throws EntityRuntimeException;	
	void setInterval(IntervalKey.YearMonth<A, T, E> k, IntervalHolder.YearMonth newValue)
		throws EntityRuntimeException;
	void setInterval(IntervalKey.DayTime<A, T, E> k, IntervalHolder.DayTime newValue)
		throws EntityRuntimeException;
		
	public E self();
	
	public E copy();

	E unify(IdentityContext ctx);
		
	boolean isIdentified() throws EntityRuntimeException;
		
}
