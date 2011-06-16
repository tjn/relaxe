/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.ent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityRuntimeException;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.model.ChangeListener;
import fi.tnie.db.model.DefaultMutableValueModel;
import fi.tnie.db.model.MutableValueModel;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.PrimitiveType;
import fi.tnie.db.types.ReferenceType;

public abstract class DefaultAttributeModelMap<
	A extends Attribute,
	T extends ReferenceType<T, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>,
	V extends Serializable,
	P extends PrimitiveType<P>,
	H extends PrimitiveHolder<V, P>,
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

	
	public <
		K extends fi.tnie.db.ent.value.PrimitiveKey<A,T,E,V,P,H,K>
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
		K extends PrimitiveKey<A, T, E, V, P, H, K>
	> 
	MutableValueModel<H> createValueModel(K k, H initialValue) {
		MutableValueModel<H> nm = new DefaultMutableValueModel<H>(initialValue);
		return nm;
	}
	
	public abstract E getTarget();
}
