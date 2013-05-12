/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.model.ent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityRuntimeException;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.model.ChangeListener;
import com.appspot.relaxe.model.DefaultMutableValueModel;
import com.appspot.relaxe.model.MutableValueModel;
import com.appspot.relaxe.rpc.AbstractPrimitiveHolder;
import com.appspot.relaxe.types.AbstractPrimitiveType;
import com.appspot.relaxe.types.ReferenceType;

public abstract class DefaultAttributeModelMap<
	A extends Attribute,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, C>,
	E extends Entity<A, ?, T, E, ?, ?, ?, C>,
	V extends Serializable,
	P extends AbstractPrimitiveType<P>,
	H extends AbstractPrimitiveHolder<V, P, H>,
	C extends Content,
	D extends AttributeModelMap<A, V, P, H, T, E, D>	
	>
	implements AttributeModelMap<A, V, P, H, T, E, D> {
	 
			
	public DefaultAttributeModelMap() {		
	}	

	private Map<A, MutableValueModel<H>> modelMap;

	private Map<A, MutableValueModel<H>> getValueModelMap() {
		if (modelMap == null) {
			modelMap = new HashMap<A, MutableValueModel<H>>();			
		}

		return modelMap;
	}
	
//	public ValueModel<String> getVarcharModel(A a) {
//		final VarcharKey<A, T, E> k = VarcharKey.get(target.getMetaData(), a);		
//		return (k == null) ? null : attr(k);
//	}
	
//	public StringModel getCharModel(A a) {
//		final CharKey<A, E> k = CharKey.get(target.getMetaData(), a);		
//		return (k == null) ? null : attr(k);
//	}

	
	@Override
	public <
		K extends com.appspot.relaxe.ent.value.PrimitiveKey<A,E,V,P,H,K>
	> 
	MutableValueModel<H> attr(final K k) throws EntityRuntimeException {
		if (k == null) {
			throw new NullPointerException("key");
		}
		
		Map<A, MutableValueModel<H>> mm = getValueModelMap();		
		MutableValueModel<H> m = mm.get(k.name());
		
		if (m == null) {
			H h = k.get(getTarget());
			MutableValueModel<H> nm = createValueModel(k, h);
									
			nm.addChangeHandler(new ChangeListener<H>() {				
				@Override
				public void changed(H from, H to) {
					getTarget().set(k, to);
				}
			});
			
			m = nm;			
			mm.put(k.name(), nm);		
		}
		
		return m;
	}

	public <		 
		K extends PrimitiveKey<A, E, V, P, H, K>
	> 
	MutableValueModel<H> createValueModel(K k, H initialValue) {
		MutableValueModel<H> nm = new DefaultMutableValueModel<H>(initialValue);
		return nm;
	}
	
	public abstract E getTarget();
}
