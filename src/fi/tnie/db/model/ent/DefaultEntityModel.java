/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.ent;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.CharType;
import fi.tnie.db.types.DateType;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.VarcharType;

public abstract class DefaultEntityModel<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, ?>,	
	E extends Entity<A, R, T, E, ?, ?, ?>,	
	D extends EntityModel<A, T, E, D>
>
	implements EntityModel<A, T, E, D> {	
	
	private E target;
	
	public E getTarget() {
		return target;
	}
	
	public DefaultEntityModel(E target) {
		super();
		
		if (target == null) {
			throw new NullPointerException("target");
		}
		
		this.target = target;
	}
	
	public <
		V extends java.io.Serializable, 
		P extends fi.tnie.db.types.PrimitiveType<P>, 
		H extends fi.tnie.db.rpc.PrimitiveHolder<V,P>, 
		K extends fi.tnie.db.ent.value.PrimitiveKey<A,T,E,V,P,H,K>
	> 
	ValueModel<H> getValueModel(K k) {				
		return k.getAttributeModel(self());
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

	public ValueModel<IntegerHolder> getIntegerModel(IntegerKey<A, T, E> key) {
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
	
	public ValueModel<VarcharHolder> getVarcharModel(VarcharKey<A, T, E> key) {
		if (key == null) {
			throw new NullPointerException("key");
		}
						
		return getVarcharAttributeModel().attr(key);
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
	
	public ValueModel<CharHolder> getCharModel(CharKey<A, T, E> key) {
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


}
