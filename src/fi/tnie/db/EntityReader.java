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
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.EntityQuery;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.types.ReferenceType;

public class EntityReader<
	A extends Attribute,
	R extends Reference,
	T extends ReferenceType<T, M>,
	E extends Entity<A, R, T, E, ?, ?, M>,
	M extends EntityMetaData<A, R, T, E, ?, ?, M>
>
	extends EntityBuilder<A, R, T, E, M> {
	
	private List<E> content;
	
	public EntityReader(ValueExtractorFactory vef, EntityQuery<A, R, T, E, M> query) {
		this(vef, query, new ArrayList<E>());
	}

	public EntityReader(ValueExtractorFactory vef, EntityQuery<A, R, T, E, M> query, List<E> result) {
		super(vef, query);
		
		if (result == null) {
			throw new NullPointerException("result");
		}
		
		this.content = result;
	}
	
	@Override
	public void process(E e) {
		content.add(e);
	}

	public List<E> getContent() {
		return content;
	}		
}