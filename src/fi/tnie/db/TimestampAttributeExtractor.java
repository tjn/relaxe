/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.TimestampType;

public class TimestampAttributeExtractor<
	A extends Attribute, 
	R,
	T extends ReferenceType<T>,
	E extends Entity<A, R, T, E>>
	extends AttributeExtractor<A, R, T, E, Date, TimestampType, TimestampHolder, TimestampKey<A, R, T, E>> {

	public TimestampAttributeExtractor(A attribute, EntityMetaData<A, R, T, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getTimestampKey(attribute), col);
	}

	@Override
	protected ValueExtractor<Date, TimestampType, TimestampHolder> createValueExtractor(ValueExtractorFactory vef, int col) {
		return vef.createTimestampExtractor(col);
	}
}
