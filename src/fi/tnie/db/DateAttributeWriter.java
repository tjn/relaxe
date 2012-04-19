/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.DateKey;
import fi.tnie.db.rpc.DateHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.DateType;
import fi.tnie.db.types.ReferenceType;

public class DateAttributeWriter<
	A extends Attribute,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>
>
	extends AbstractAttributeWriter<A, T, E, Date, DateType, DateHolder, DateKey<A, T, E>>
{
	private DateExtractor extractor;
	
	public DateAttributeWriter(DateKey<A, T, E> key, int index) {
		super(key, index);
		this.extractor = new DateExtractor(index);
	}
	
	/**
	 * Returns {@link PrimitiveHolder#asDateHolder()}. 
	 */	
	@Override
	protected DateHolder as(PrimitiveHolder<?, ?> ph) {
		return ph.asDateHolder();
	}
	
	
	@Override
	protected DateHolder extract(ResultSet src) 
		throws SQLException {
		return this.extractor.extractValue(src);
	}
	
}
