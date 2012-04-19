/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.TimestampKey;
import fi.tnie.db.rpc.TimestampHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.TimestampType;
import fi.tnie.db.types.ReferenceType;

public class TimestampAttributeWriter<
	A extends Attribute,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>
>
	extends AbstractAttributeWriter<A, T, E, Date, TimestampType, TimestampHolder, TimestampKey<A, T, E>>
{
	private TimestampExtractor extractor;
	
	public TimestampAttributeWriter(TimestampKey<A, T, E> key, int index) {
		super(key, index);
		this.extractor = new TimestampExtractor(index);
	}
	
	/**
	 * Returns {@link PrimitiveHolder#asTimestampHolder()}. 
	 */	
	@Override
	protected TimestampHolder as(PrimitiveHolder<?, ?> ph) {
		return ph.asTimestampHolder();
	}
	
	
	@Override
	protected TimestampHolder extract(ResultSet src) 
		throws SQLException {
		return this.extractor.extractValue(src);
	}
	
}
