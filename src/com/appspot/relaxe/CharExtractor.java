/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.CharHolder;
import com.appspot.relaxe.types.CharType;


class CharExtractor
	extends ValueExtractor<String, CharType, CharHolder>
{
	public CharExtractor(int column) {
		super(column);			
	}
	
	@Override
	public CharHolder extractValue(ResultSet rs) throws SQLException {
		String s = rs.getString(getColumn());
		return CharHolder.valueOf(s);
	}
}