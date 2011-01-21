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
import fi.tnie.db.types.TimestampType;

public class TimestampAttributeExtractor<A extends Attribute, E extends Entity<A,?,?,E>>
	extends AttributeExtractor<Date, TimestampType, TimestampHolder, A, E, TimestampKey<A, E>> {

	public TimestampAttributeExtractor(A attribute, EntityMetaData<A, ?, ?, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getTimestampKey(attribute), col);
	}

	@Override
	protected ValueExtractor<Date, TimestampType, TimestampHolder> createValueExtractor(ValueExtractorFactory vef, int col) {
		return vef.createTimestampExtractor(col);
	}
}
