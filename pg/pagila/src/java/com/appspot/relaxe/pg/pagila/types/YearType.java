/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila.types;

import com.appspot.relaxe.types.DistinctType;

public class YearType
	extends DistinctType<YearType> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4915976457434883692L;
	public static final YearType TYPE = new YearType();
	
	private YearType() {
		super();
	}

	@Override
	public String getName() {
		return "year";
	}

	@Override
	public YearType self() {
		return this;
	}
}
