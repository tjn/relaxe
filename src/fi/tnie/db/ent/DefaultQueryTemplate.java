/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.expr.OrderBy;
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
	implements EntityQueryTemplate<A, R, T, E, H, F, M, Q>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5086377201591899922L;
	
	private Map<A, EntityQueryTemplateAttribute> attributeMap;
	private Map<R, EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?>> templateMap;
	
	private List<EntityQuerySortKey<A>> sortKeyList = null;	
	private List<EntityQuerySortKey<?>> allSortKeys = null;
	
	@Override
	public EntityQueryTemplateAttribute get(A a) throws EntityRuntimeException {
		return getAttributeMap().get(a);
	}	

	@Override
	public abstract M getMetaData();

	@Override
	public EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?> getTemplate(EntityKey<R, T, E, M, ?, ?, ?, ?, ?, ?, ?, ?> k) {
		return getTemplateMap().get(k.name());
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
		getTemplateMap().put(k.name(), newTemplate);
	}
	
	private Map<A, EntityQueryTemplateAttribute> getAttributeMap() {
		if (attributeMap == null) {
			attributeMap = new HashMap<A, EntityQueryTemplateAttribute>();			
		}

		return attributeMap;
	}
		
	private Map<
		R, 
		EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?>
	> 
	getTemplateMap() {
		if (templateMap == null) {
			templateMap = new HashMap<R, EntityQueryTemplate<?,?,?,?,?,?,?,?>
			>();			
		}

		return templateMap;
	}
	
	public void add(A attribute) {
		add(attribute, getAttributeMap());
	}
	
	private void add(A a, Map<A, EntityQueryTemplateAttribute> dest) {
		dest.put(a, DefaultTemplateAttribute.get(a));
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
		// EntityQueryTemplateAttribute marker = DefaultTemplateAttribute.get();
	
		for (A a : as) {
			if (!am.containsKey(a)) {
				add(a, am);				
			}
		}
	}
	
	public void set(A a, EntityQueryTemplateAttribute t) {
		getAttributeMap().put(a, t);
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
	public EntityQuery<A, R, T, E, H, F, M, Q> newQuery() throws CyclicTemplateException {	
		return newQuery(null, null);
	}
	
	public EntityQuery<A, R, T, E, H, F, M, Q> newQuery(long limit, long offset) 
		throws CyclicTemplateException {
		return newQuery(Long.valueOf(limit), Long.valueOf(offset));
	}
	
	
	public void asc(A sk) {
		addSortKey(SortKeyAttributeTemplate.asc(sk));
	}
	
	public void desc(A sk) {
		addSortKey(SortKeyAttributeTemplate.desc(sk));
	}
	
	public void addSortKey(EntityQuerySortKey<A> sk) {
		getSortKeyList().add(sk);
		getAllSortKeys().add(sk);
	}	
	
	@Override
	public List<EntityQuerySortKey<A>> sortKeys() {
		if (this.sortKeyList == null) {
			return Collections.emptyList();
		}
				
		return Collections.unmodifiableList(this.sortKeyList);
	}
	
	private List<EntityQuerySortKey<A>> getSortKeyList() {
		if (sortKeyList == null) {
			sortKeyList = new ArrayList<EntityQuerySortKey<A>>();			
		}

		return sortKeyList;
	}

	public <
		SA extends Attribute,
		SQ extends EntityQueryTemplate<SA, ?, ?, ?, ?, ?, ?, ?>
	>	
	void addSortKey(SQ template, SA attribute, OrderBy.Order so) {	
		EntityQuerySortKey<SA> sk = SortKeyAttributeTemplate.get(attribute, so);
		template.addSortKey(sk);
		
		if (template != this) {
			getAllSortKeys().add(sk);
		}		
			
	}
	
	
	public <
		SA extends Attribute,
		SQ extends EntityQueryTemplate<SA, ?, ?, ?, ?, ?, ?, ?>
	>
	void asc(SQ template, SA attribute) {
		addSortKey(template, attribute, OrderBy.Order.ASC);
	}
	
	public <
		SA extends Attribute,
		SQ extends EntityQueryTemplate<SA, ?, ?, ?, ?, ?, ?, ?>
	>
	void desc(SQ template, SA attribute) {
		addSortKey(template, attribute, OrderBy.Order.DESC);
	}

	
	@Override
	public List<EntityQuerySortKey<?>> allSortKeys() {
		return Collections.unmodifiableList(getAllSortKeys());
	}	
	
	private List<EntityQuerySortKey<?>> getAllSortKeys() {
		if (allSortKeys == null) {
			allSortKeys = new ArrayList<EntityQuerySortKey<?>>();			
		}

		return allSortKeys;
	}

}
