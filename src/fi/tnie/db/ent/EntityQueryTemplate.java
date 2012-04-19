/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.ent;

import java.io.Serializable;
import java.util.List;

import fi.tnie.db.ent.value.EntityKey;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public interface EntityQueryTemplate<
	A extends Attribute,
	R extends Reference,	
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	Q extends EntityQueryTemplate<A, R, T, E, H, F, M, C, Q>
	> 
	extends
	Serializable
{
	
	M getMetaData();		
	Q self();
	
	EntityQueryTemplateAttribute get(A a)
		throws EntityRuntimeException;

	EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ?, ?> getTemplate(EntityKey<A, R, T, E, H, F, M, C, ?, ?, ?, ?, ?, ?, ?, ?, ?> k);

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
	void setTemplate(RK k, RQ newTemplate);
	
	public void addAllAttributes();
	
	public void add(A attribute);
	
	public void add(A ... attributes);	
	public void remove(A ... attributes);
	
	EntityQuery<A, R, T, E, H, F, M, C, Q> newQuery()
		throws CyclicTemplateException;

	List<EntityQuerySortKey<A>> sortKeys();	
	List<EntityQuerySortKey<?>> allSortKeys();
	
	List<EntityQueryPredicate<A>> predicates();
	List<EntityQueryPredicate<?>> allPredicates();

	void addSortKey(EntityQuerySortKey<A> sk);	
	void addPredicate(EntityQueryPredicate<A> p);
}

