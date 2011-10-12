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
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>,
	Q extends EntityQueryTemplate<A, R, T, E, H, F, M, Q>
	> 
	extends
	Serializable
{
	
	M getMetaData();		
	Q self();
	
	EntityQueryTemplateAttribute get(A a)
		throws EntityRuntimeException;

	EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ? > getTemplate(EntityKey<A, R, T, E, H, F, M, ?, ?, ?, ?, ?, ?, ?, ?> k);

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
	void setTemplate(RK k, RQ newTemplate);
	
	public void addAllAttributes();
	
	public void add(A attribute);
	
	public void add(A ... attributes);	
	public void remove(A ... attributes);
	
	EntityQuery<A, R, T, E, H, F, M, Q> newQuery()
		throws CyclicTemplateException;

	List<EntityQuerySortKey<A>> sortKeys();	
	List<EntityQuerySortKey<?>> allSortKeys();
	
	List<EntityQueryPredicate<A>> predicates();
	List<EntityQueryPredicate<?>> allPredicates();

	void addSortKey(EntityQuerySortKey<A> sk);	
	void addPredicate(EntityQueryPredicate<A> p);
}

