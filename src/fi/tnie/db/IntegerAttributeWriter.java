/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.IntegerKey;
import fi.tnie.db.rpc.IntegerHolder;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.types.IntegerType;
import fi.tnie.db.types.ReferenceType;

public class IntegerAttributeWriter<
	A extends Attribute,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>
>
	extends AbstractAttributeWriter<A, T, E, Integer, IntegerType, IntegerHolder, IntegerKey<A, T, E>>	
{
	private IntegerExtractor extractor;
	
	public IntegerAttributeWriter(IntegerKey<A, T, E> key, int index) {
		super(key, index);
		this.extractor = new IntegerExtractor(index);
	}
	
	/**
	 * Returns {@link PrimitiveHolder#asIntegerHolder()}. 
	 */	
	@Override
	protected IntegerHolder as(PrimitiveHolder<?, ?> ph) {
		return ph.asIntegerHolder();
	}
	
	
	@Override
	protected IntegerHolder extract(ResultSet src) 
		throws SQLException {
		return this.extractor.extractValue(src);
	}
	
	
	
//	@Override
//	protected PrimitiveHolder<?, ?> extract(ResultSet src, int index) 
//		throws SQLException {	
//		IntegerHolder h = this.extractor.extractValue(src);
//		return h;
//	}
}
