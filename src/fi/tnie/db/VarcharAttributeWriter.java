/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.ent.Attribute;
import fi.tnie.db.ent.Entity;
import fi.tnie.db.ent.value.VarcharKey;
import fi.tnie.db.rpc.PrimitiveHolder;
import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.ReferenceType;
import fi.tnie.db.types.VarcharType;

public class VarcharAttributeWriter<
	A extends Attribute,
	T extends ReferenceType<A, ?, T, E, ?, ?, ?, ?>,
	E extends Entity<A, ?, T, E, ?, ?, ?, ?>
>
	extends AbstractAttributeWriter<A, T, E, String, VarcharType, VarcharHolder, VarcharKey<A, T, E>>
{
	private VarcharExtractor extractor;
	
	public VarcharAttributeWriter(VarcharKey<A, T, E> key, int index) {
		super(key, index);
		this.extractor = new VarcharExtractor(index);
	}
	
	/**
	 * Returns {@link PrimitiveHolder#asVarcharHolder()}. 
	 */	
	@Override
	protected VarcharHolder as(PrimitiveHolder<?, ?> ph) {
		return ph.asVarcharHolder();
	}
		
	
	@Override
	protected VarcharHolder extract(ResultSet src) 
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
