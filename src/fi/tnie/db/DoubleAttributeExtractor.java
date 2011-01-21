/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.io.Serializable;

import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.DoubleKey;
import fi.tnie.db.rpc.DoubleHolder;
import fi.tnie.db.types.DoubleType;

public class DoubleAttributeExtractor<A extends Serializable, E extends Entity<A,?,?,E>>
	extends AttributeExtractor<Double, DoubleType, DoubleHolder, A, E, DoubleKey<A, E>> {

	public DoubleAttributeExtractor(A attribute, EntityMetaData<A, ?, ?, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getDoubleKey(attribute), col);
	}

	@Override
	protected ValueExtractor<Double, DoubleType, DoubleHolder> createValueExtractor(ValueExtractorFactory vef, int col) {		
		return vef.createDoubleExtractor(col);
	}
}
