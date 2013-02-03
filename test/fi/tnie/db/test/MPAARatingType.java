/*
 * Copyright (c) 2009-2013 Topi Nieminen
 */
package fi.tnie.db.test;


import fi.tnie.db.types.EnumType;

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
	public Class<MPAARating> represents() {		
		return MPAARating.class;
	}	
}
