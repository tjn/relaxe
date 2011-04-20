/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.Reference;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.types.DoubleType;
import fi.tnie.db.types.ReferenceType;

public class DoubleAttributeExtractor<
	A extends Attribute, 
	R extends Reference,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>
>
	extends AttributeExtractor<A, R, T, E, Double, DoubleType, DoubleHolder, DoubleKey<A, R, T, E>> {

	public DoubleAttributeExtractor(A attribute, EntityMetaData<A, R, T, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getDoubleKey(attribute), col);
	}

	@Override
	protected ValueExtractor<Double, DoubleType, DoubleHolder> createValueExtractor(ValueExtractorFactory vef, int col) {
		return vef.createDoubleExtractor(col);
	}
}
