/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;


import java.util.Set;

import fi.tnie.db.ent.im.EntityIdentityMap;
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
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public interface EntityMetaData<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,	
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>
> {

	Class<A> getAttributeNameType();

	Class<R> getReferenceNameType();

	/**
	 * Returns the base table this meta-data is bound to or <code>null</code>
	 * if this meta-data instance is not bound to any table.
	 *
	 * @return  The base table
	 */
	BaseTable getBaseTable();

	F getFactory();
	
	EntityBuilder<E> newBuilder(TableReference tableRef, EntityBuildContext ctx);
		
	EntityIdentityMap<A, R, T, E> getIdentityMap(IdentityContext ctx);
	E unify(IdentityContext ctx, E e) throws EntityRuntimeException;
	void dispose(IdentityContext ctx);
		
//	PrimitiveKey<A, ?, ?, ?, E> key(A a);


//	<
//		S extends Serializable,
//		P extends PrimitiveType<P>,
//		H extends PrimitiveHolder<S, P>,
//		K extends Key<A, S, P, H, E>,
//		V extends Value<A, S, P, H, E, K>,
//		F extends ValueFactory<A, S, P, H, E, K, V>
//
//	>
//	F newValueFactory(K a);


	/**
	 * Unmodifiable set containing the names of the attributes which are applicable to entities this object represents.
	 * @return
	 */
	Set<A> attributes();
	Set<R> relationships();

	Column getColumn(A a);
	A getAttribute(Column c);
	
	/**
	 * Type of the attribute <code>name</code>
	 * @param name
	 * @return
	 */
	PrimitiveType<?> getAttributeType(A name);

//	Key<A, ?, ?, ?, E, ?> getKey(Column c);
	PrimitiveKey<A, T, E, ?, ?, ?, ?> getKey(A a);
		
	EntityKey<R, T, E, M, ?, ?, ?, ?, ?> getEntityKey(R ref);
	
//	<
//		X extends fi.tnie.db.ent.Attribute,
//		Y extends fi.tnie.db.ent.Reference,		
//		Z extends ReferenceType<Z, O>,
//		V extends Entity<X, Y, Z, V, VH, ?, O>,
//		VH extends ReferenceHolder<X, Y, Z, V, VH, O>,
//		O extends EntityMetaData<X, Y, Z, V, VH, ?, O>
//	>		
//	EntityKey<R, T, E, M, Z, V, VH, O, ?> getEntityKey(R ref, O target);

	
	
//	<
//		X extends Attribute,
//		Y extends Reference,
//		Z extends ReferenceType<Z>,
//		V extends Entity<X, Y, Z, V>,
//		W extends ReferenceHolder<X, Y, Z, V>
//	>
//	EntityKey<A, R, T, E, Z, V, ?, ?> getEntityKey(R ref, EntityMetaData<X, Y, Z, V, W> target);
	
//	<
//		X extends ReferenceType<X>,
//		W extends Entity<?, ?, X, W>
//	>
//	EntityKey<A, R, T, E, X, W, ?, ?> getEntityKey(R ref, X type, W value);
	

	IntegerKey<A, T, E> getIntegerKey(A a);
	VarcharKey<A, T, E> getVarcharKey(A a);
	DateKey<A, T, E> getDateKey(A a);
	DecimalKey<A, T, E> getDecimalKey(A a);
	DoubleKey<A, T, E> getDoubleKey(A a);
	CharKey<A, T, E> getCharKey(A a);
	TimestampKey<A, T, E> getTimestampKey(A a);
	TimeKey<A, T, E> getTimeKey(A a);
	IntervalKey.YearMonth<A, T, E> getYearMonthIntervalKey(A a);
	IntervalKey.DayTime<A, T, E> getDayTimeIntervalKey(A a);
	
	
	ForeignKey getForeignKey(R r);
	Set<Column> getPKDefinition();

	void bind(BaseTable table)
		throws EntityException;

	/**
	 * Returns a set of the references the column <code>c</code> is part of.
	 *
	 * @param c
	 * @return
	 */
	Set<R> getReferences(Column c);

	/**
	 * Returns an object identical to getBaseTable().getSchema().getCatalog()
	 * @return
	 */
	Catalog getCatalog();

	T getType();
	
//	public EntityBuilder<E> newBuilder();

	void addKey(DecimalKey<A, T, E> key);
	void addKey(DoubleKey<A, T, E> key);
	void addKey(IntegerKey<A, T, E> key);
	void addKey(CharKey<A, T, E> key);
	void addKey(DateKey<A, T, E> key);
	void addKey(VarcharKey<A, T, E> key);
	void addKey(TimestampKey<A, T, E> key);	
	void addKey(TimeKey<A, T, E> key);
	void addKey(IntervalKey.YearMonth<A, T, E> key);
	void addKey(IntervalKey.DayTime<A, T, E> key);

//	<
//		X extends fi.tnie.db.ent.Attribute,
//		Y extends fi.tnie.db.ent.Reference,
//		Z extends ReferenceType<Z>,
//		V extends Entity<X, Y, Z, V>,
//		W extends ReferenceHolder<X, Y, Z, V>,
//		O extends EntityMetaData<X, Y, Z, V, W, O>
//	>	
//	EntityKey<X, Y, Z, V, T, E, ?, ?> newKey(Y ref, O target);

	// IdentityContext createIdentityContext();
	
	M self();
	
//	<
//		X extends Attribute,		
//		Y extends Reference,
//		Z extends ReferenceType<Z, S>,
//		VH extends ReferenceHolder<X, Y, Z, V, VH, S>,
//		V extends Entity<X, Y, Z, V, VH, ?, S>,
//		S extends EntityMetaData<X, Y, Z, V, VH, ?, S>
//	>	
//	EntityKey<Y, Z, V, S, T, E, H, M, ?> newKey(Y ref, S source);
	
}
