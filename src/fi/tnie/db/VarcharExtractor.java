/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package fi.tnie.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import fi.tnie.db.rpc.VarcharHolder;
import fi.tnie.db.types.VarcharType;

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