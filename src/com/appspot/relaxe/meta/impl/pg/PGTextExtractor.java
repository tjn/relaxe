/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
/**
 * 
 */
package com.appspot.relaxe.meta.impl.pg;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.appspot.relaxe.ValueExtractor;
import com.appspot.relaxe.env.pg.PGTextHolder;
import com.appspot.relaxe.env.pg.PGTextType;

public class PGTextExtractor
	extends ValueExtractor<String, PGTextType, PGTextHolder> {

	public PGTextExtractor(int column) {
		super(column);
	}

	@Override
	public PGTextHolder extractValue(ResultSet rs) throws SQLException {
		String s = rs.getString(getColumn());
		return PGTextHolder.valueOf(s);
	}
}