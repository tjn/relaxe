/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.ent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appspot.relaxe.ent.value.EntityKey;
import com.appspot.relaxe.ent.value.PrimitiveKey;
import com.appspot.relaxe.expr.OrderBy;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;

public abstract class DefaultQueryTemplate<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	Q extends EntityQueryTemplate<A,R,T,E,H,F,M,C,Q>
>
	implements EntityQueryTemplate<A, R, T, E, H, F, M, C, Q>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5086377201591899922L;
	
	private Map<A, EntityQueryTemplateAttribute> attributeMap;
	private Map<R, EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?>> templateMap;
	
	private List<EntityQuerySortKey<A>> sortKeyList = null;	
	private List<EntityQuerySortKey<?>> allSortKeys = null;
	private List<EntityQueryPredicate<A>> predicateList = null;	
	private List<EntityQueryPredicate<?>> allPredicates = null;
	
//	private E predicateSource;
	
	@Override
	public EntityQueryTemplateAttribute get(A a) throws EntityRuntimeException {
		return getAttributeMap().get(a);
	}	

	@Override
	public abstract M getMetaData();

	@Override
	public EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> getTemplate(EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k) {
		return getTemplateMap().get(k.name());
	}

	@Override
	public abstract Q self();

	@Override
	public 
	<			
		RA extends Attribute,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM, RC>,
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM, RC>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM, RC>,
		RF extends EntityFactory<RE, RH, RM, RF, RC>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM, RC>,
		RC extends Content,
		RK extends EntityKey<A, R, T, E, H, F, M, C, RA, RR, RT, RE, RH, RF, RM, RC, RK>,
		RQ extends EntityQueryTemplate<RA, RR, RT, RE, RH, RF, RM, RC, RQ>
	>	 
	void setTemplate(RK k, RQ newTemplate) {
		getTemplateMap().put(k.name(), newTemplate);
	};
	
	private Map<A, EntityQueryTemplateAttribute> getAttributeMap() {
		if (attributeMap == null) {
			attributeMap = new HashMap<A, EntityQueryTemplateAttribute>();			
		}

		return attributeMap;
	}
		
	private Map<
		R, 
		EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?>
	> 
	getTemplateMap() {
		if (templateMap == null) {
			templateMap = new HashMap<R, EntityQueryTemplate<?,?,?,?,?,?,?,?,?>
			>();			
		}

		return templateMap;
	}
		
	public void add(PrimitiveKey<A, E, ?, ?, ?, ?> ak) {
		add(ak.name());
	}
	
	@Override
	public void add(A attribute) {
		add(attribute, getAttributeMap());
	}
	
	private void add(A a, Map<A, EntityQueryTemplateAttribute> dest) {
		dest.put(a, DefaultTemplateAttribute.get());
	}
	
	
	public void add(PrimitiveKey<A, E, ?, ?, ?, ?>[]  ks) {
		for (PrimitiveKey<A, E, ?, ?, ?, ?> k : ks) {
			add(k.name());
		}		
	}
	
	@Override
	public void add(Iterable<A>  as) {
		if (as == null) {
			throw new NullPointerException("as");
		}
		
		if (!as.iterator().hasNext()) {
			return;
		}
		
		addAll(as);
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
	
	@Override
	public void remove(Iterable<A> as) {
		if (as == null) {
			throw new NullPointerException("as");
		}
		
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
	public abstract EntityQuery<A, R, T, E, H, F, M, C, Q> newQuery()
		throws EntityRuntimeException;
			
	
	
	public void asc(A sk) {
		addSortKey(SortKeyAttributeTemplate.asc(sk));
	}
	
	public void desc(A sk) {
		addSortKey(SortKeyAttributeTemplate.desc(sk));
	}
	
	@Override
	public void addSortKey(EntityQuerySortKey<A> sk) {
		getSortKeyList().add(sk);
		getAllSortKeys().add(sk);
	}	
	
	@Override
	public void addPredicate(EntityQueryPredicate<A> p) {
		getPredicateList().add(p);
		getAllPredicates().add(p);
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
	
	private List<EntityQueryPredicate<A>> getPredicateList() {
		if (predicateList == null) {
			predicateList = new ArrayList<EntityQueryPredicate<A>>();			
		}

		return predicateList;
	}
	
	
	

	public <
		SA extends Attribute,
		SQ extends EntityQueryTemplate<SA, ?, ?, ?, ?, ?, ?, ?, ?>
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
		SQ extends EntityQueryTemplate<SA, ?, ?, ?, ?, ?, ?, ?, ?>
	>
	void asc(SQ template, SA attribute) {
		addSortKey(template, attribute, OrderBy.Order.ASC);
	}
	
	public <
		SA extends Attribute,
		SQ extends EntityQueryTemplate<SA, ?, ?, ?, ?, ?, ?, ?, ?>
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
	
	@Override
	public List<EntityQueryPredicate<?>> allPredicates() {
		return Collections.unmodifiableList(getAllPredicates());
	}
	
	private List<EntityQueryPredicate<?>> getAllPredicates() {
		if (allPredicates == null) {
			allPredicates = new ArrayList<EntityQueryPredicate<?>>();			
		}

		return allPredicates;
	}
	
	@Override
	public List<EntityQueryPredicate<A>> predicates() {
		return Collections.unmodifiableList(getPredicateList());
	}
	
	@Override
	public int getTemplateCount() {
		return (templateMap == null) ? 0 : templateMap.size();
	}
}
