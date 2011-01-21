/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.EntityMetaData;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.types.DateType;

public class DateAttributeExtractor<A extends Attribute, E extends Entity<A,?,?,E>>
	extends AttributeExtractor<Date, DateType, DateHolder, A, E, DateKey<A, E>> {

	public DateAttributeExtractor(A attribute, EntityMetaData<A, ?, ?, E> meta, ValueExtractorFactory vef, int col) {
		super(vef, attribute, meta.getDateKey(attribute), col);
	}

	@Override
	protected ValueExtractor<Date, DateType, DateHolder> createValueExtractor(ValueExtractorFactory vef, int col) {
		return vef.createDateExtractor(col);
	}
}
