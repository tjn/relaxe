/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.ent.value.LongVarBinary;
import com.appspot.relaxe.rpc.LongVarBinaryHolder;
import com.appspot.relaxe.types.LongVarBinaryType;


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