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
import fi.tnie.db.ent.Content;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityDataObject;
import fi.tnie.db.ent.EntityFactory;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.UnificationContext;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.query.QueryException;
import fi.tnie.db.rpc.ReferenceHolder;
import fi.tnie.db.types.ReferenceType;

public class EntityReader<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content
>
	extends EntityBuilderManager<A, R, T, E, H, F, M, C> {

	private List<EntityDataObject<E>> content;

	public EntityReader(ValueExtractorFactory vef, EntityQuery<A, R, T, E, H, F, M, C, ?> query, UnificationContext unificationContext)
		throws QueryException {
		this(vef, query, new ArrayList<EntityDataObject<E>>(), unificationContext);
	}

	public EntityReader(ValueExtractorFactory vef, EntityQuery<A, R, T, E, H, F, M, C, ?> query, List<EntityDataObject<E>> result, UnificationContext identityContext)
		throws QueryException {
		super(vef, query, identityContext);

		if (result == null) {
			throw new NullPointerException("result");
		}

		this.content = result;
//		this.identityMap = query.getMetaData().getIdentityMap(identityContext);
	}

	@Override
	public void process(EntityDataObject<E> e) {
		content.add(e);
	}

	public List<EntityDataObject<E>> getContent() {
		return content;
	}

}