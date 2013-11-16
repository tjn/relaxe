/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package com.appspot.relaxe.pg.pagila.types;


import com.appspot.relaxe.types.EnumType;

public class MPAARatingType
	extends EnumType<MPAARatingType, MPAARating> {

	public static final MPAARatingType TYPE = new MPAARatingType();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -973907139465998994L;
	
	private MPAARatingType() {		
	}

	@Override
	public String getName() {
		return "mpaa_rating";
	}

	@Override
	public MPAARatingType self() {
		return this;
	}

	@Override
	public Class<MPAARating> getValueType() {		
		return MPAARating.class;
	}	
	
	
}