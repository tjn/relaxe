/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.ent;

import java.io.Serializable;
import java.util.Map;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDiff;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.IdentityContext;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.DecimalKey;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.TimeKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.meta.Column;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.DecimalHolder;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.rpc.IntervalHolder.DayTime;
import fi.tnie.db.rpc.IntervalHolder.YearMonth;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public abstract class EntityFilter<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>	
>
	implements Entity<A, R, T, E, H, F, M>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4107350733444109193L;
	private E inner;
		
	/**
	 * No-argument constructor for GWT Serialization
	 */
	@SuppressWarnings("unused")
	private EntityFilter() {
	}

	public EntityFilter(E inner) {
		super();
		this.inner = inner;
	}

	@Override
	public EntityDiff<A, R, T, E> diff(E another) throws EntityRuntimeException {
		return this.inner.diff(another);
	}

	@Override
	public PrimitiveHolder<?, ?> get(Column c) throws NullPointerException,
			EntityRuntimeException {
		return this.inner.get(c);
	}

	@Override
	public <
		S extends Serializable, 
		P extends PrimitiveType<P>, 
		PH extends PrimitiveHolder<S, P>, 
		K extends PrimitiveKey<A, T, E, S, P, PH, K>
	> 
	PH get(K k) throws EntityRuntimeException {
		return inner.get(k);
	}

	@Override
	public CharHolder getChar(CharKey<A, T, E> k) throws EntityRuntimeException {		
		return inner.getChar(k);
	}

	@Override
	public DateHolder getDate(DateKey<A, T, E> k) throws EntityRuntimeException {		
		return inner.getDate(k);
	}

	@Override
	public DecimalHolder getDecimal(DecimalKey<A, T, E> k)
			throws EntityRuntimeException {		
		return inner.getDecimal(k);
	}

	@Override
	public DoubleHolder getDouble(DoubleKey<A, T, E> k)
			throws EntityRuntimeException {
		return inner.getDouble(k);
	}

	@Override
	public IntegerHolder getInteger(IntegerKey<A, T, E> k)
			throws EntityRuntimeException {
		return inner.getInteger(k);
	}

	@Override
	public DayTime getInterval(
			fi.tnie.db.ent.value.IntervalKey.DayTime<A, T, E> k) {
		return inner.getInterval(k);
	}

	@Override
	public YearMonth getInterval(
			fi.tnie.db.ent.value.IntervalKey.YearMonth<A, T, E> k) {
		return inner.getInterval(k);
	}

	@Override
	public M getMetaData() {
		return inner.getMetaData();
	}

	@Override
	public Map<Column, PrimitiveHolder<?, ?>> getPrimaryKey()
			throws EntityRuntimeException {
		return inner.getPrimaryKey();
	}

	@Override
	public <
		P extends ReferenceType<P, D>, 
		G extends Entity<?, ?, P, G, RH, ?, D>, 
		RH extends ReferenceHolder<?, ?, P, G, RH, D>, 
		D extends EntityMetaData<?, ?, P, G, RH, ?, D>, 
		K extends EntityKey<R, T, E, M, P, G, RH, D, K>
	> 
	RH getRef(K k) {
		return inner.getRef(k);
	}

	@Override
	public Entity<?, ?, ?, ?, ?, ?, ?> getRef(R k) {
		return inner.getRef(k);
	}

	@Override
	public TimeHolder getTime(TimeKey<A, T, E> k) throws EntityRuntimeException {
		return inner.getTime(k);
	}

	@Override
	public TimestampHolder getTimestamp(TimestampKey<A, T, E> k)
			throws EntityRuntimeException {
		return inner.getTimestamp(k);
	}

	@Override
	public T getType() {
		return inner.getType();
	}

	@Override
	public VarcharHolder getVarchar(VarcharKey<A, T, E> k)
			throws EntityRuntimeException {
		return inner.getVarchar(k);
	}

	@Override
	public boolean isIdentified() throws EntityRuntimeException {
		return inner.isIdentified();
	}

	@Override
	public H ref() {
		return inner.ref();
	}

	@Override
	public ReferenceHolder<?, ?, ?, ?, ?, ?> ref(R ref) {
		return inner.ref(ref);
	}

	@Override
	public <
		S extends Serializable, 
		P extends PrimitiveType<P>, RH extends PrimitiveHolder<S, P>, K extends PrimitiveKey<A, T, E, S, P, RH, K>> void set(
			K k, RH newValue) throws EntityRuntimeException {
		inner.set(k, newValue);		
	}

	@Override
	public void setChar(CharKey<A, T, E> k, CharHolder newValue)
			throws EntityRuntimeException {
		inner.setChar(k, newValue);		
	}

	@Override
	public void setDate(DateKey<A, T, E> k, DateHolder newValue)
			throws EntityRuntimeException {
		inner.setDate(k, newValue);
	}

	@Override
	public void setDecimal(DecimalKey<A, T, E> k, DecimalHolder newValue)
			throws EntityRuntimeException {
		inner.setDecimal(k, newValue);		
	}

	@Override
	public void setDouble(DoubleKey<A, T, E> k, DoubleHolder newValue)
			throws EntityRuntimeException {
		inner.setDouble(k, newValue);		
	}

	@Override
	public void setInteger(IntegerKey<A, T, E> k, IntegerHolder newValue)
			throws EntityRuntimeException {
		inner.setInteger(k, newValue);		
	}

	@Override
	public void setInterval(
			fi.tnie.db.ent.value.IntervalKey.DayTime<A, T, E> k,
			DayTime newValue) throws EntityRuntimeException {
		inner.setInterval(k, newValue);	
	}

	@Override
	public void setInterval(
			fi.tnie.db.ent.value.IntervalKey.YearMonth<A, T, E> k,
			YearMonth newValue) throws EntityRuntimeException {
		inner.setInterval(k, newValue);		
	}

	@Override
	public <P extends ReferenceType<P, D>, G extends Entity<?, ?, P, G, RH, ?, D>, RH extends ReferenceHolder<?, ?, P, G, RH, D>, D extends EntityMetaData<?, ?, P, G, RH, ?, D>, K extends EntityKey<R, T, E, M, P, G, RH, D, K>> void setRef(
			K k, RH newValue) {
		inner.setRef(k, newValue);		
	}

	@Override
	public void setTime(TimeKey<A, T, E> k, TimeHolder newValue)
			throws EntityRuntimeException {
		inner.setTime(k, newValue);		
	}

	@Override
	public void setTimestamp(TimestampKey<A, T, E> k, TimestampHolder newValue)
			throws EntityRuntimeException {
		inner.setTimestamp(k, newValue);		
	}

	@Override
	public void setVarchar(VarcharKey<A, T, E> k, VarcharHolder newValue)
			throws EntityRuntimeException {
		inner.setVarchar(k, newValue);		
	}

	@Override
	public E unify(IdentityContext ctx) {
		return inner.unify(ctx);
	}

	@Override
	public PrimitiveHolder<?, ?> value(A attribute)
			throws EntityRuntimeException {		
		return inner.value(attribute);
	}
}
