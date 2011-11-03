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
import fi.tnie.db.ent.value.PrimitiveKey;
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
	public EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?> getTemplate(EntityKey<A, R, T, E, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?> k) {
		return getTemplateMap().get(k.name());
	}

	@Override
	public abstract Q self();

	@Override
	public 
	<			
		RA extends Attribute,
		RR extends Reference,
		RT extends ReferenceType<RA, RR, RT, RE, RH, RF, RM>,
		RE extends Entity<RA, RR, RT, RE, RH, RF, RM>,
		RH extends ReferenceHolder<RA, RR, RT, RE, RH, RM>,
		RF extends EntityFactory<RE, RH, RM, RF>,
		RM extends EntityMetaData<RA, RR, RT, RE, RH, RF, RM>,		
		RK extends EntityKey<A, R, T, E, H, F, M, RA, RR, RT, RE, RH, RF, RM, RK>,
		RQ extends EntityQueryTemplate<RA, RR, RT, RE, RH, RF, RM, RQ>
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
		EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?>
	> 
	getTemplateMap() {
		if (templateMap == null) {
			templateMap = new HashMap<R, EntityQueryTemplate<?,?,?,?,?,?,?,?>
			>();			
		}

		return templateMap;
	}
		
	public void add(PrimitiveKey<A, T, E, ?, ?, ?, ?> ak) {
		add(ak.name());
	}
	
	public void add(A attribute) {
		add(attribute, getAttributeMap());
	}
	
	private void add(A a, Map<A, EntityQueryTemplateAttribute> dest) {
		dest.put(a, DefaultTemplateAttribute.get());
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
	
	public void add(PrimitiveKey<A, T, E, ?, ?, ?, ?>[]  ks) {
		for (PrimitiveKey<A, T, E, ?, ?, ?, ?> k : ks) {
			add(k.name());
		}		
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
		
	public abstract EntityQuery<A, R, T, E, H, F, M, Q> newQuery()
		throws EntityRuntimeException;
			
	
	
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
}
