/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.util.ArrayList;
import java.util.List;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityException;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.env.Implementation;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class EntityReader<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M>,
	E extends Entity<A, R, T, E, H, F, M>,
	H extends ReferenceHolder<A, R, T, E, H, M>,
	F extends EntityFactory<E, H, M, F>,
	M extends EntityMetaData<A, R, T, E, H, F, M>
>
	extends EntityBuilderManager<A, R, T, E, H, F, M> {
	
	private List<EntityDataObject<E>> content;
	
	public EntityReader(Implementation imp, EntityQuery<A, R, T, E, H, F, M, ?> query) throws EntityException {
		this(imp, query, new ArrayList<EntityDataObject<E>>());
	}

	public EntityReader(Implementation imp, EntityQuery<A, R, T, E, H, F, M, ?> query, List<EntityDataObject<E>> result) 
		throws EntityException {
		super(imp, query);
		
		if (result == null) {
			throw new NullPointerException("result");
		}
		
		this.content = result;
	}
	
	@Override
	public void process(EntityDataObject<E> e) {
		content.add(e);
	}

	public List<EntityDataObject<E>> getContent() {
		return content;
	}
	
}