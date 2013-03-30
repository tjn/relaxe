/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.ent.value.LongVarBinary;
import fi.tnie.db.rpc.LongVarBinaryHolder;
import fi.tnie.db.types.LongVarBinaryType;

public class LongVarBinaryExtractor
	extends ValueExtractor<LongVarBinary, LongVarBinaryType, LongVarBinaryHolder>
{
	public LongVarBinaryExtractor(int column) {
		super(column);			
	}

	@Override
	public LongVarBinaryHolder extractValue(ResultSet rs) throws SQLException {
		byte[] content = rs.getBytes(getColumn());
		
		if (content == null) {
			return LongVarBinaryHolder.NULL_HOLDER;
		}		
		
		LongVarBinary blob = new LongVarBinary.Builder(content).newLongVarBinary();
		LongVarBinaryHolder holder = LongVarBinaryHolder.valueOf(blob);		
		return holder;
	}
}