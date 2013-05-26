/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila;

import com.appspot.relaxe.types.Enumerable;

public enum MPAARating
	implements Enumerable {
	G,
	PG,
	PG_13,
	R,
	NC_17
	;

	@Override
	public String value() {
		return toString().replace('_', '-');
	}
//    'G',
//    'PG',
//    'PG-13',
//    'R',
//    'NC-17'

	
	public static MPAARating parse(String s) {
		String n = s.replace('-', '_');
		return MPAARating.valueOf(n);
	}
}