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

	EntityQueryTemplate<?, ?, ?, ?, ?, ?, ?, ? > getTemplate(EntityKey<R, T, E, M, ?, ?, ?, ?, ?, ?, ?, ?> k);

	<	
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
	void setTemplate(K k, VQ newTemplate);
	
	public void addAllAttributes();
	
	public void add(A attribute);
	
	public void add(A ... attributes);	
	public void remove(A ... attributes);
	
	EntityQuery<A, R, T, E, H, F, M, Q> newQuery()
		throws CyclicTemplateException;

	List<EntityQuerySortKey<A>> sortKeys();
	
	List<EntityQuerySortKey<?>> allSortKeys();

	void addSortKey(EntityQuerySortKey<A> sk);
}

