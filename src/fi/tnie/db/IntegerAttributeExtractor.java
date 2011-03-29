/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.ReferenceType;

public class IntegerAttributeExtractor<
	A extends Attribute, 
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>>
	extends AttributeExtractor<A, R, T, E, Integer, IntegerType, IntegerHolder, IntegerKey<A, R, T, E>> {

	public IntegerAttributeExtractor(A attribute, EntityMetaData<A, R, T, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getIntegerKey(attribute), col);
	}

	@Override
	protected ValueExtractor<Integer, IntegerType, IntegerHolder> createValueExtractor(ValueExtractorFactory vef, int col) {
		return vef.createIntegerExtractor(col);
	}
}
