/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.ent;

import java.io.Serializable;
import java.util.Date;
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
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntervalAccessor;
import fi.tnie.db.ent.value.IntervalKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.ent.value.IntervalAccessor.DayTime;
import fi.tnie.db.meta.Column;
// import fi.tnie.db.model.MutableValueModel;
import fi.tnie.db.model.MutableValueModel;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.CharType;
import fi.tnie.db.types.DateType;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.TimestampType;
import fi.tnie.db.types.VarcharType;

public abstract class DefaultEntityModel<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	D extends EntityModel<A, R, T, E, H, F, M, D>
>
	implements EntityModel<A, R, T, E, H, F, M, D> {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6797576262563930996L;
	
	private E target;
	
	public DefaultEntityModel(E target) {
		super();
		
		if (target == null) {
			throw new NullPointerException("target");
		}
		
		this.target = target;
	}
	
	@Override
	public H ref() {
		return target.ref();
	}
	
	@Override
	public EntityDiff<A, R, T, E> diff(E another) throws EntityRuntimeException {
		return target.diff(another);
	}
	
	@Override
	public E unify(IdentityContext ctx) {
		return target.unify(ctx);
	}
	
	public fi.tnie.db.rpc.PrimitiveHolder<?,?> value(A attribute) throws EntityRuntimeException {
		return target.value(attribute);		
	}

	@Override
	public boolean isIdentified() throws EntityRuntimeException {
		return target.isIdentified();
	}
	
	@Override
	public M getMetaData() {
		return target.getMetaData();
	}
	
	@Override
	public Map<Column, PrimitiveHolder<?, ?>> getPrimaryKey() throws EntityRuntimeException {
		return target.getPrimaryKey();
	}
	
	public <
		V extends java.io.Serializable, 
		P extends fi.tnie.db.types.PrimitiveType<P>, 
		VH extends fi.tnie.db.rpc.PrimitiveHolder<V,P>, 
		K extends fi.tnie.db.ent.value.PrimitiveKey<A,T,E,V,P,VH,K>
	> 
	ValueModel<VH> getValueModel(K k) {				
		return k.getAttributeModel(asModel());
	};
	
		
	private IntegerAttributeModel integerAttributeModel;

	private class IntegerAttributeModel 
		extends DefaultAttributeModelMap<A, T, E, Integer, IntegerType, IntegerHolder, IntegerAttributeModel>
	{
		@Override
		public E getTarget() {		
			return target;
		}
		
		@Override
		public IntegerAttributeModel self() {
			return this;
		}		
	}

	public ValueModel<IntegerHolder> getIntegerModel(IntegerKey<A, T, E> key) throws EntityRuntimeException {
		if (key == null) {
			throw new NullPointerException("key");
		}
						
		return getIntegerAttributeModel().attr(key);
	}



	private IntegerAttributeModel getIntegerAttributeModel() {
		if (integerAttributeModel == null) {
			integerAttributeModel = new IntegerAttributeModel();			
		}

		return integerAttributeModel;
	}
	
	@Override
	public IntegerHolder getInteger(IntegerKey<A, T, E> k) {
		return getValueModel(k).get();
	}
	
	// next type	
	private VarcharAttributeModel varcharAttributeModel;
	
	private class VarcharAttributeModel 
		extends DefaultAttributeModelMap<A, T, E, String, VarcharType, VarcharHolder, VarcharAttributeModel>
	{
		@Override
		public E getTarget() {		
			return target;
		}
		
		@Override
		public VarcharAttributeModel self() {
			return this;
		}		
	}
	
	public ValueModel<VarcharHolder> getVarcharModel(A a) {
		final VarcharKey<A, T, E> k = VarcharKey.get(target.getMetaData(), a);
		return (k == null) ? null : getVarcharModel(k);
	}
	
	public ValueModel<VarcharHolder> getVarcharModel(VarcharKey<A, T, E> key) throws EntityRuntimeException {
		if (key == null) {
			throw new NullPointerException("key");
		}
						
		return getVarcharAttributeModel().attr(key);
	}
	
	public VarcharHolder getVarchar(VarcharKey<A, T, E> key) {		
		return getValueModel(key).get();
	}
	
	@Override
	public void setVarchar(VarcharKey<A, T, E> k, VarcharHolder newValue) 
		throws EntityRuntimeException {
		assign(k, newValue);		
	}
	
	
	private <
		K extends PrimitiveKey<A, T, E, V, PT, PH, K>,
		V extends Serializable,
		PT extends PrimitiveType<PT>,
		PH extends PrimitiveHolder<V, PT>
	>	
	void assign(K k, PH newValue)
		throws EntityRuntimeException {
		MutableValueModel<PH> mm = getValueModel(k).asMutable();		
		
		if (mm == null) {
			throw new EntityRuntimeException();
		}
		
		mm.set(newValue);
	}
	

	private VarcharAttributeModel getVarcharAttributeModel() {
		if (varcharAttributeModel == null) {
			varcharAttributeModel = new VarcharAttributeModel();			
		}

		return varcharAttributeModel;
	}

	// next type	
	private CharAttributeModel charAttributeModel;
	
	private class CharAttributeModel 
		extends DefaultAttributeModelMap<A, T, E, String, CharType, CharHolder, CharAttributeModel>
	{
		@Override
		public E getTarget() {		
			return target;
		}
		
		@Override
		public CharAttributeModel self() {
			return this;
		}		
	}
	
	public ValueModel<CharHolder> getCharModel(A a) {
		final CharKey<A, T, E> k = CharKey.get(target.getMetaData(), a);
		return (k == null) ? null : getCharModel(k);
	}
	
	public ValueModel<CharHolder> getCharModel(CharKey<A, T, E> key) throws EntityRuntimeException {
		if (key == null) {
			throw new NullPointerException("key");
		}
						
		return getCharAttributeModel().attr(key);
	}

	private CharAttributeModel getCharAttributeModel() {
		if (charAttributeModel == null) {
			charAttributeModel = new CharAttributeModel();			
		}

		return charAttributeModel;
	}
	
	@Override
	public CharHolder getChar(CharKey<A, T, E> k) {
		return getValueModel(k).get();
	}
	
	@Override
	public void setChar(CharKey<A, T, E> k, CharHolder newValue) {
		assign(k, newValue);		
	}


	// next type	
	private DateAttributeModel dateAttributeModel;
	
	private class DateAttributeModel 
		extends DefaultAttributeModelMap<A, T, E, Date, DateType, DateHolder, DateAttributeModel>
	{
		@Override
		public E getTarget() {		
			return target;
		}
		
		@Override
		public DateAttributeModel self() {
			return this;
		}		
	}
	
	public ValueModel<DateHolder> getDateModel(A a) {
		final DateKey<A, T, E> k = DateKey.get(target.getMetaData(), a);
		return (k == null) ? null : getDateModel(k);
	}
	
	public ValueModel<DateHolder> getDateModel(DateKey<A, T, E> key) {
		if (key == null) {
			throw new NullPointerException("key");
		}
						
		return getDateAttributeModel().attr(key);
	}

	private DateAttributeModel getDateAttributeModel() {
		if (dateAttributeModel == null) {
			dateAttributeModel = new DateAttributeModel();			
		}

		return dateAttributeModel;
	}
	
	@Override
	public DateHolder getDate(DateKey<A, T, E> k) {
		return getValueModel(k).get();
	}

	// next type	
	private TimestampAttributeModel timestampAttributeModel;
	
	private class TimestampAttributeModel 
		extends DefaultAttributeModelMap<A, T, E, Date, TimestampType, TimestampHolder, TimestampAttributeModel>
	{
		@Override
		public E getTarget() {		
			return target;
		}
		
		@Override
		public TimestampAttributeModel self() {
			return this;
		}		
	}
	
	public ValueModel<TimestampHolder> getTimestampModel(A a) {
		final TimestampKey<A, T, E> k = TimestampKey.get(target.getMetaData(), a);
		return (k == null) ? null : getTimestampModel(k);
	}
	
	public ValueModel<TimestampHolder> getTimestampModel(TimestampKey<A, T, E> key) {
		if (key == null) {
			throw new NullPointerException("key");
		}
						
		return getTimestampAttributeModel().attr(key);
	}

	private TimestampAttributeModel getTimestampAttributeModel() {
		if (timestampAttributeModel == null) {
			timestampAttributeModel = new TimestampAttributeModel();			
		}

		return timestampAttributeModel;
	}
	
	@Override
	public TimestampHolder getTimestamp(TimestampKey<A, T, E> k) {
		return getValueModel(k).get();
	}
	
	
	public IntervalAccessor.DayTime<A, T, E> getIntervalAccessor(IntervalKey.DayTime<A, T, E> k) {
		DayTime<A, T, E> ia = new IntervalAccessor.DayTime<A, T, E>(self(), k);
		return ia;
	}
	
}
