/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public abstract class DefaultQueryTemplate<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	Q extends EntityQueryTemplate<A,R,T,E,H,F,M,Q>
>
	implements EntityQueryTemplate<A, R, T, E, H, F, M, Q>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5086377201591899922L;
	private Map<A, EntityQueryTemplateAttribute> attributeMap;
	private Map<EntityKey<R, T, E, M, ?, ?, ?, ?, ?, ?, ?, ?>, EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?>> templateMap;
	
	 
	
	@Override
	public EntityQueryTemplateAttribute get(A a) throws EntityRuntimeException {
		return getAttributeMap().get(a);
	}	

	@Override
	public abstract M getMetaData();

	@Override
	public EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?> getTemplate(EntityKey<R, T, E, M, ?, ?, ?, ?, ?, ?, ?, ?> k) {
		return getTemplateMap().get(k);
	}

	@Override
	public abstract Q self();

	
	@Override
	public <
		VT extends ReferenceType<VA, VR, VT, V, RH, VF, D>, 
		VA extends Attribute, 
		VR extends Reference, 
		V extends Entity<VA, VR, VT, V, RH, VF, D>, 
		RH extends ReferenceHolder<VA, VR, VT, V, RH, D>, 
		VF extends EntityFactory<V, RH, D, VF>, 
		D extends EntityMetaData<VA, VR, VT, V, RH, VF, D>, 
		K extends EntityKey<R, T, E, M, VT, VA, VR, V, RH, VF, D, K>, 
		VQ extends EntityQueryTemplate<VA, VR, VT, V, RH, VF, D, VQ>
	> 
	void setTemplate(K k, VQ newTemplate) {
		getTemplateMap().put(k, newTemplate);
	}
	
	private Map<A, EntityQueryTemplateAttribute> getAttributeMap() {
		if (attributeMap == null) {
			attributeMap = new HashMap<A, EntityQueryTemplateAttribute>();			
		}

		return attributeMap;
	}
		
	private Map<
		EntityKey<R, T, E, M, ?, ?, ?, ?, ?, ?, ?, ?>, 
		EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?>
	> 
	getTemplateMap() {
		if (templateMap == null) {
			templateMap = new HashMap<
				EntityKey<R,T,E,M,?,?,?,?,?,?,?,?>, 
				EntityQueryTemplate<?,?,?,?,?,?,?,?>
			>();			
		}

		return templateMap;
	}
	
	@Override
	public void remove(A ... as) {
		if (as == null) {
			throw new NullPointerException("as");
		}
		
		if (as.length == 0) {
			return;
		}
		
		remove(Arrays.asList(as));
	}
	
	public void add(A ... as) {
		if (as == null) {
			throw new NullPointerException("as");
		}
		
		if (as.length == 0) {
			return;
		}
		
		addAll(Arrays.asList(as));
	}

	private void addAll(Iterable<A> as) {
		Map<A, EntityQueryTemplateAttribute> am = getAttributeMap();
		EntityQueryTemplateAttribute marker = DefaultTemplateAttribute.getInstance();
	
		for (A a : as) {
			if (!am.containsKey(a)) {
				am.put(a, marker);
			}
		}
	}
	
	public void remove(Iterable<A> as) {
		Map<A, EntityQueryTemplateAttribute> am = getAttributeMap();
		
		for (A a : as) {
			if (!am.containsKey(a)) {
				am.remove(a);
			}
		}		
	}
	
	@Override
	public void addAllAttributes() {
		addAll(getMetaData().attributes());		
	}
	
	@Override
	public EntityQuery<A, R, T, E, M> newQuery() throws CyclicTemplateException {	
		return newQuery(0, 0);
	}
}
