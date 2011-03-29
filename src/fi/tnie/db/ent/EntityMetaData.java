/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;


import java.util.Set;

import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DecimalKey;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntervalKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.TimeKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Catalog;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public interface EntityMetaData<
	A extends Attribute,
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
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

	EntityFactory<A, R, T, E> getFactory();

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
	 * Unmodifiable set containing the names of the attributes which are applicable to entities this object describes.
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
	PrimitiveKey<A, R, T, E, ?, ?, ?, ?> getKey(A a);

	IntegerKey<A, R, T, E> getIntegerKey(A a);
	VarcharKey<A, R, T, E> getVarcharKey(A a);
	DateKey<A, R, T, E> getDateKey(A a);
	DecimalKey<A, R, T, E> getDecimalKey(A a);
	DoubleKey<A, R, T, E> getDoubleKey(A a);
	CharKey<A, R, T, E> getCharKey(A a);
	TimestampKey<A, R, T, E> getTimestampKey(A a);
	TimeKey<A, R, T, E> getTimeKey(A a);
	
	IntervalKey.YearMonth<A, R, T, E> getYearMonthIntervalKey(A a);
	IntervalKey.DayTime<A, R, T, E> getDayTimeIntervalKey(A a);

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

	void addKey(DecimalKey<A, R, T, E> key);
	void addKey(DoubleKey<A, R, T, E> key);
	void addKey(IntegerKey<A, R, T, E> key);
	void addKey(CharKey<A, R, T, E> key);
	void addKey(DateKey<A, R, T, E> key);
	void addKey(VarcharKey<A, R, T, E> key);
	void addKey(TimestampKey<A, R, T, E> key);	
	void addKey(TimeKey<A, R, T, E> key);
	void addKey(IntervalKey.YearMonth<A, R, T, E> key);
	void addKey(IntervalKey.DayTime<A, R, T, E> key);
	
}
