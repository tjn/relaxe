/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.CharKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.CharHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.CharType;

public class CharAttributeWriter<
	A extends Attribute,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>
>
	extends AbstractAttributeWriter<A, T, E, String, CharType, CharHolder, CharKey<A, T, E>>
{
	private CharExtractor extractor;
	
	public CharAttributeWriter(CharKey<A, T, E> key, int index) {
		super(key, index);
		this.extractor = new CharExtractor(index);
	}
	
	/**
	 * Returns {@link PrimitiveHolder#asCharHolder()}. 
	 */	
	@Override
	protected CharHolder as(PrimitiveHolder<?, ?> ph) {
		return ph.asCharHolder();
	}
	
	
	@Override
	protected CharHolder extract(ResultSet src) 
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
