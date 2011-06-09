/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.ent;

import java.io.Serializable;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.model.ValueModel;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.VarcharHolder;
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
	
	public DefaultEntityModel(E target) {
		super();
		
		if (target == null) {
			throw new NullPointerException("target");
		}
		
		this.target = target;
	}

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
	
//	private Map<PrimitiveType<?>, AttributeModel<A, T, E, ?, ?>> modelMap;
	
	private VarcharAttributeModel varcharAttributeModel;
	private IntegerAttributeModel integerAttributeModel;
			
//	<
//		P extends PrimitiveType<P>		
//	>	
//	AttributeModel<A, T, E, ?, ?> getAttributeModel(P type) {		
//		AttributeModel<A, T, E, ?, ?> mm = modelMap.get(type);
//		
//		if (mm == null) {
//			if (type == IntegerType.TYPE) {
//				modelMap.put(type, mm = new IntegerAttributeModel());			
//			}
//			if (type == VarcharType.TYPE) {
//				modelMap.put(type, mm = getVarcharAttributeModel());			
//			}			
//		}
//						
//		return mm;
//	}
		
	public <
		H extends PrimitiveHolder<?, ?>, 
		K extends fi.tnie.db.ent.value.PrimitiveKey<A,T,E,?,?,H,K>
	> 
	ValueModel<H> get(K k) {
		return k.getAttributeModel(this);		
	};
	
	
//	public <
//		V extends Serializable, 
//		K extends fi.tnie.db.ent.value.PrimitiveKey<A,T,E,V,?,?,K>, 
//		AM extends AttributeModel<A, T, E, V, AM>
//	>		
//	AM get(K k) {
//		D d = self();
//		
//		EntityModel<A, T, E, ?> s = self();
//				
//		k.getAttributeModel(self());
//	};
	
	
//	public <
//		V extends Serializable,
//		P extends PrimitiveType<P>,
//		K extends PrimitiveKey<A, T, E, V, P, ?, K>
//	> 
//	
//	ValueModel<V> getValueModel(K key) {
//		AttributeModel<A, R, T, E, ?, ?> am = getAttributeModel(key.type());
//		D m = key.attributeModel(self());
//		
////		am.attr(key);
//				
//		return m.;		
//	}
		
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
	
	public ValueModel<IntegerHolder> getIntegerModel(IntegerKey<A, T, E> key) {
		if (key == null) {
			throw new NullPointerException("key");
		}
						
		return getIntegerAttributeModel().attr(key);
	}

	private VarcharAttributeModel getVarcharAttributeModel() {
		if (varcharAttributeModel == null) {
			varcharAttributeModel = new VarcharAttributeModel();			
		}

		return varcharAttributeModel;
	}


	private IntegerAttributeModel getIntegerAttributeModel() {
		if (integerAttributeModel == null) {
			integerAttributeModel = new IntegerAttributeModel();			
		}

		return integerAttributeModel;
	}

	
	


}
