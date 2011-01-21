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

public class IntegerAttributeExtractor<A extends Attribute, E extends Entity<A,?,?,E>>
	extends AttributeExtractor<Integer, IntegerType, IntegerHolder, A, E, IntegerKey<A, E>> {

	public IntegerAttributeExtractor(A attribute, EntityMetaData<A, ?, ?, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getIntegerKey(attribute), col);
	}

	@Override
	protected ValueExtractor<Integer, IntegerType, IntegerHolder> createValueExtractor(ValueExtractorFactory vef, int col) {
		return vef.createIntegerExtractor(col);
	}
}
