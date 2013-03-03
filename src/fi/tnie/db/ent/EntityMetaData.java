/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Set;

import fi.tnie.db.ent.im.EntityIdentityMap;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.expr.TableReference;
import fi.tnie.db.meta.BaseTable;
import fi.tnie.db.meta.Column;
import fi.tnie.db.meta.ForeignKey;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public interface EntityMetaData<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,	
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,	 
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends fi.tnie.db.ent.Content
> 	
{

	/**
	 * Returns the base table this meta-data is bound to or <code>null</code>
	 * if this meta-data instance is not bound to any table.
	 *
	 * @return  The base table
	 */
	BaseTable getBaseTable();

	F getFactory();
	
	EntityBuilder<E, H> newBuilder(TableReference referencing, ForeignKey referencedBy, TableReference tableRef, EntityBuildContext ctx, UnificationContext unificationContext)
		throws EntityException;
	
	EntityIdentityMap<A, R, T, E, H> createIdentityMap();	
	EntityIdentityMap<A, R, T, E, H> getIdentityMap(UnificationContext ctx);
	H unify(UnificationContext ctx, E e) throws EntityRuntimeException;
	void dispose(UnificationContext ctx);

	/**
	 * Unmodifiable set containing the names of the attributes which are applicable to entities this object represents.
	 * @return
	 */
	Set<A> attributes();
	Set<R> relationships();

	Column getColumn(A a);
	A getAttribute(Column c);
		
	
	PrimitiveKey<A, E, ?, ?, ?, ?> getKey(A a);
		
	/* EntityKey<R, T, E, M, ?, ? ,?, ?, ?, ?, ?, ?> getEntityKey(R ref); *)
	 * 
	 */
	
	EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> getEntityKey(R ref);
	
	
//	<
//		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM>,
//		RA extends Attribute,
//		RR extends Reference,	
//		RE extends Entity<RA, RR, RT, RE, RH, RF, RM>,
//		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
//		RF extends EntityFactory<RE, RH, RM, RF>,
//		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM>,
//		RK extends EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, RK> 
//	>
//	RK getEntityKey(RM target, R ref);

	
//	IntegerKey<A, E> getIntegerKey(A a);
//	VarcharKey<A, E> getVarcharKey(A a);
//	DateKey<A, E> getDateKey(A a);
//	DecimalKey<A, E> getDecimalKey(A a);
//	DoubleKey<A, E> getDoubleKey(A a);
//	CharKey<A, E> getCharKey(A a);
//	TimestampKey<A, E> getTimestampKey(A a);
//	TimeKey<A, E> getTimeKey(A a);
//	IntervalKey.YearMonth<A, E> getYearMonthIntervalKey(A a);
//	IntervalKey.DayTime<A, E> getDayTimeIntervalKey(A a);
		
	ForeignKey getForeignKey(R r);
	Set<Column> getPKDefinition();

//	void bind(BaseTable table)
//		throws EntityException;

	/**
	 * Returns a set of the references the column <code>c</code> is part of.
	 *
	 * @param c
	 * @return
	 */
	Set<R> getReferences(Column c);

//	/**
//	 * Returns an object identical to getBaseTable().getSchema().getCatalog()
//	 * @return
//	 */
//	Catalog getCatalog();

	T type();


//	void addKey(DecimalKey<A, E> key);
//	void addKey(DoubleKey<A, E> key);
//	void addKey(IntegerKey<A, E> key);
//	void addKey(CharKey<A, E> key);
//	void addKey(DateKey<A, E> key);
//	void addKey(VarcharKey<A, E> key);
//	void addKey(TimestampKey<A, E> key);	
//	void addKey(TimeKey<A, E> key);
//	void addKey(IntervalKey.YearMonth<A, E> key);
//	void addKey(IntervalKey.DayTime<A, E> key);
	
	M self();
}
