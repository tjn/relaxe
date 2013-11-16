/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.appspot.relaxe.rpc.StringArray;
import com.appspot.relaxe.rpc.VarcharArrayHolder;
import com.appspot.relaxe.types.VarcharArrayType;


public class VarcharArrayExtractor
	extends ValueExtractor<StringArray, VarcharArrayType, VarcharArrayHolder>
{	
	
	public VarcharArrayExtractor(int column) {
		super(column);			
	}

	@Override
	public VarcharArrayHolder extractValue(ResultSet rs) throws SQLException {
		Array array = rs.getArray(getColumn());
		
		if (array == null) {
			return VarcharArrayHolder.NULL_HOLDER;
		}


		ResultSet as = null;
		List<String> elems = new ArrayList<String>();

		try {			
			as = array.getResultSet();
						
			while (as.next()) {
				String elem = as.getString(2);
				elems.add(elem);
			}
		}
		finally {
			as.close();
		}	
		
		// TODO: probably fails on PG, hide behind interface
		array.free();
		
		StringArray av = new StringArray(elems);		
		return new VarcharArrayHolder(av);			
	}
}