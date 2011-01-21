/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.Map;

import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.CharValue;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DateValue;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntegerValue;
import fi.tnie.db.ent.value.Key;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.TimestampValue;
import fi.tnie.db.ent.value.Value;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.ent.value.VarcharValue;
import fi.tnie.db.meta.Column;
	
public interface Entity<
	A extends Serializable,
	R,	 
	T extends ReferenceType<T>, 
	E extends Entity<A, R, T, E>
> 
	extends
	Serializable
{	
	/**
	 * Returns a value of the attribute <code>a</code>  
	 * @param r
	 * @return
	 */	
//	Value<A, ?, ?, ?> value(A a);
			
//	<	
//		K extends Key<A, ?, ?, ?, E, ?>
//	>	
//	Value<A, ?, ?, ?, E, ?> value(K k);
	
	
	<	
		S extends Serializable,
		P extends PrimitiveType<P>,
		H extends PrimitiveHolder<S, P>,
		K extends Key<A, S, P, H, E, K>
	>	
	Value<A, S, P, H, E, K> value(K k);
	
	
	Value<A, ?, ?, ?, E, ?> value(A attribute);
			
	
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
//	ReferenceHolder<?, ?> get(ForeignKey fk);
		
	ReferenceHolder<?, ?, ?, ?> ref(R ref);
	
	
//	/** 
//	 * Should we do type checking by exposing accessors only?
//	 * 
//	 * Set the value of the attribute <code>a</code>
//	 * 
//	 * @param a
//	 * @param value
//	 */
//	void set(A a, PrimitiveHolder<?, ?> value);
	
	
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
	
	IntegerValue<A, E> integerValue(IntegerKey<A, E> k);	
	VarcharValue<A, E> varcharValue(VarcharKey<A, E> k);
	DateValue<A, E> dateValue(DateKey<A, E> k);
	TimestampValue<A, E> timestampValue(TimestampKey<A, E> k);
	CharValue<A, E> charValue(CharKey<A, E> k);
		
	/**
	 * TODO: EntityQueryTask should be also rewritten to use DataObjectReader
	 */
	// DataObject getSource();
}
