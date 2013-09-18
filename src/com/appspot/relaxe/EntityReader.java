/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 *
 */
package com.appspot.relaxe;

import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.ent.Attribute;
import com.appspot.relaxe.ent.Content;
import com.appspot.relaxe.ent.Entity;
import com.appspot.relaxe.ent.EntityDataObject;
import com.appspot.relaxe.ent.EntityFactory;
import com.appspot.relaxe.ent.EntityMetaData;
import com.appspot.relaxe.ent.EntityQueryElement;
import com.appspot.relaxe.ent.Reference;
import com.appspot.relaxe.ent.UnificationContext;
import com.appspot.relaxe.query.QueryException;
import com.appspot.relaxe.rpc.ReferenceHolder;
import com.appspot.relaxe.types.ReferenceType;


public class EntityReader<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<A, R, T, E, H, F, M, C>,
	E extends Entity<A, R, T, E, H, F, M, C>,
	H extends ReferenceHolder<A, R, T, E, H, M, C>,
	F extends EntityFactory<E, H, M, F, C>,
	M extends EntityMetaData<A, R, T, E, H, F, M, C>,
	C extends Content,
	QE extends EntityQueryElement<A, R, T, E, H, F, M, C, QE>
>
	extends EntityBuilderManager<A, R, T, E, H, F, M, C, QE> {

	private List<EntityDataObject<E>> content;

	public EntityReader(ValueExtractorFactory vef, EntityQueryExpressionBuilder<A, R, T, E, H, F, M, C, QE> builder, UnificationContext unificationContext)
		throws QueryException {
		this(vef, builder, new ArrayList<EntityDataObject<E>>(), unificationContext);
	}

	public EntityReader(ValueExtractorFactory vef, EntityQueryExpressionBuilder<A, R, T, E, H, F, M, C, QE> builder, List<EntityDataObject<E>> result, UnificationContext identityContext)
		throws QueryException {
		super(vef, builder, identityContext);

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