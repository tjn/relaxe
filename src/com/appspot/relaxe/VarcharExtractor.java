/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.rpc.VarcharHolder;
import com.appspot.relaxe.types.VarcharType;


public class VarcharExtractor
	extends ValueExtractor<String, VarcharType, VarcharHolder>
{
	public VarcharExtractor(int column) {
		super(column);			
	}
	
	@Override
	public VarcharHolder extractValue(ResultSet rs) throws SQLException {
		String s = rs.getString(getColumn());
		return VarcharHolder.valueOf(s);
	}
}