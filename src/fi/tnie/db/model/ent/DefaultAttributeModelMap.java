/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.model.ent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.PrimitiveKey;
import fi.tnie.db.model.ChangeListener;
import fi.tnie.db.model.DefaultMutableValueModel;
import fi.tnie.db.model.ValueModel;
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

	private Map<A, ValueModel<H>> modelMap;

	private Map<A, ValueModel<H>> getValueModelMap() {
		if (modelMap == null) {
			modelMap = new HashMap<A, ValueModel<H>>();			
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
	ValueModel<H> attr(final K k) {
		if (k == null) {
			throw new NullPointerException("key");
		}
		
		Map<A, ValueModel<H>> mm = getValueModelMap();		
		ValueModel<H> m = mm.get(k.name());
		
		if (m == null) {
			H h = k.get(getTarget());
			ValueModel<H> nm = createValueModel(k, h);
									
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
	ValueModel<H> createValueModel(K k, H v) {
		ValueModel<H> nm = new DefaultMutableValueModel<H>(v);
		return nm;
	}
	
	public abstract E getTarget();
}
