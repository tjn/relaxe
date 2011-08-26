/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.ent;

import java.io.Serializable;
import java.util.Date;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateAccessor;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.IntervalAccessor;
import fi.tnie.db.ent.value.IntervalKey;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.ent.value.IntervalAccessor.DayTime;
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
	
	public fi.tnie.db.rpc.PrimitiveHolder<?,?> value(A attribute) throws EntityRuntimeException {
		return target.value(attribute);		
	}
	
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
	
	
	public <
		V extends Serializable, 
		P extends fi.tnie.db.types.PrimitiveType<P>, 
		PH extends fi.tnie.db.rpc.PrimitiveHolder<V,P>, 
		K extends fi.tnie.db.ent.value.PrimitiveKey<A,T,E,V,P,PH,K>
	> 
	fi.tnie.db.model.ValueModel<PH> getValueModel(K k) throws EntityRuntimeException {
		ValueModel<PH> vm = k.getAttributeModel(this);
		return vm;
	};
	
		
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
	

	protected <
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
	
	protected <
		K extends EntityKey<R, T, E, M, VT, VE, VH, VM, K>,
		VT extends ReferenceType<VT, VM>,
		VE extends Entity<?, ?, VT, VE, VH, ?, VM>,
		VH extends ReferenceHolder<?, ?, VT, VE, VH, VM>,
		VM extends EntityMetaData<?, ?, VT, VE, VH, ?, VM>
	>	
	void assign(K k, VH newValue)
		throws EntityRuntimeException {
		MutableValueModel<VH> mm = getEntityModel(k).asMutable();
		
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
			
	public IntervalAccessor.DayTime<A, T, E> getIntervalAccessor(IntervalKey.DayTime<A, T, E> k) {
		DayTime<A, T, E> ia = new IntervalAccessor.DayTime<A, T, E>(asEntity(), k);
		return ia;
	}

	public DateAccessor<A, T, E> getDateAccessor(DateKey<A, T, E> k) {
		DateAccessor<A, T, E> ia = new DateAccessor<A, T, E>(asEntity(), k);
		return ia;
	}

	
//	public <
//		V extends Serializable, 
//		P extends fi.tnie.db.types.PrimitiveType<P>, 
//		PH extends fi.tnie.db.rpc.PrimitiveHolder<V,P>, 
//		K extends fi.tnie.db.ent.value.PrimitiveKey<A,T,E,V,P,PH,K>
//	>
//	fi.tnie.db.model.ValueModel<PH> getValueModel(K k) throws EntityRuntimeException {
//				
//		return k.getAttributeModel(this);		
//	};
	

//	@Override
//	public <
//		V extends Serializable, 
//		P extends PrimitiveType<P>, 
//		PH extends PrimitiveHolder<V, P>, 
//		K extends PrimitiveKey<A, T, E, V, P, PH, K>
//	> 
//	ValueModel<PH> getValueModel(K k) throws EntityRuntimeException {
//				
//		new ValueModelKey<A, T, E, V, P, PH, K>(k);
//		
//		
//		return mk.getAttributeModel(this);
//	}

	
	protected E getTarget() {
		return target;
	}

}
