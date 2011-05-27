/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.TimeKey;
import fi.tnie.db.rpc.TimeHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.TimeType;
import fi.tnie.db.types.ReferenceType;

public class TimeAttributeWriter<
	A extends Attribute,
	T extends ReferenceType<T, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?>
>
	extends AbstractAttributeWriter<A, T, E, Date, TimeType, TimeHolder, TimeKey<A, T, E>>
{
	private TimeExtractor extractor;
	
	public TimeAttributeWriter(TimeKey<A, T, E> key, int index) {
		super(key, index);
		this.extractor = new TimeExtractor(index);
	}
	
	/**
	 * Returns {@link PrimitiveHolder#asTimeHolder()}. 
	 */	
	@Override
	protected TimeHolder as(PrimitiveHolder<?, ?> ph) {
		return ph.asTimeHolder();
	}
	
	
	@Override
	protected TimeHolder extract(ResultSet src) 
		throws SQLException {
		return this.extractor.extractValue(src);
	}
	
}
